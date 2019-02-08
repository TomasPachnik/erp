package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.*;
import sk.tomas.erp.entity.AssetEntity;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.AssetRepository;
import sk.tomas.erp.repository.InvoiceRepository;
import sk.tomas.erp.repository.LegalRepository;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.service.InvoiceService;
import sk.tomas.erp.util.Utils;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static sk.tomas.erp.util.Utils.createdUpdated;
import static sk.tomas.erp.validator.BaseValidator.validateUuid;
import static sk.tomas.erp.validator.InvoiceServiceValidator.validateInvoice;
import static sk.tomas.erp.validator.InvoiceServiceValidator.validatePagingInput;

@Slf4j
@Service
@MethodCallLogger
public class InvoiceServiceImpl implements InvoiceService {

    private ModelMapper mapper;
    private final UserServiceImpl userService;
    private InvoiceRepository invoiceRepository;
    private final LegalRepository legalRepository;
    private final AssetRepository assetRepository;
    private AuditService auditService;

    private final List<String> tableProperties;

    @Autowired
    public InvoiceServiceImpl(ModelMapper mapper, InvoiceRepository invoiceRepository,
                              UserServiceImpl userService, LegalRepository legalRepository,
                              AssetRepository assetRepository, AuditService auditService) {
        this.mapper = mapper;
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.legalRepository = legalRepository;
        this.assetRepository = assetRepository;
        this.auditService = auditService;
        tableProperties = new LinkedList<>();
        fillTableProperties();
    }

    @Override
    @Transactional
    public Invoice get(UUID uuid) {
        return getInvoice(uuid, userService.getLoggedUser().getUuid());
    }

    @Override
    public List<Invoice> all() {
        List<InvoiceEntity> all = invoiceRepository.all(userService.getLoggedUser().getUuid());
        Type listType = new TypeToken<List<Invoice>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public Paging all(PagingInput input) {
        validatePagingInput(input);
        Page<InvoiceEntity> page = invoiceRepository.findByOwner(userService.getLoggedUser().getUuid(),
                PageRequest.of(input.getPageIndex(), input.getPageSize(), getSortFromInput(input)));

        Paging paging = new Paging();
        paging.setTotal((int) page.getTotalElements());

        paging.setPageable(new PagingOutput(page.getPageable().getPageNumber(), page.getPageable().getPageSize()));
        Type listType = new TypeToken<List<Invoice>>() {
        }.getType();
        paging.setContent(mapper.map(page.getContent(), listType));
        return paging;
    }

    @Override
    @Transactional
    public boolean deleteByUuid(UUID uuid) {
        validateUuid(uuid);
        try {
            InvoiceEntity invoiceEntity = invoiceRepository.findByUuid(uuid, userService.getLoggedUser().getUuid());
            if (invoiceEntity != null) {
                String name = invoiceEntity.getName();
                String invoiceEntityString = Utils.toJson(invoiceEntity, InvoiceEntity.class);
                invoiceRepository.delete(invoiceEntity);
                auditService.log(InvoiceEntity.class, userService.getLoggedUser().getUuid(), invoiceEntityString, null);
                log.info(MessageFormat.format("Invoice ''{0}'' was deleted by ''{1}''.", name, userService.getLoggedUser().getUsername()));
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public UUID save(InvoiceInput invoiceInput) {
        validateInvoice(invoiceInput);
        UserEntity loggedUser = userService.getLoggedUser();
        InvoiceEntity oldInvoice = null;
        String invoiceEntityOldString = null;
        //if updating entry, check, if updater is owner
        if (invoiceInput.getUuid() != null) {
            oldInvoice = invoiceRepository.findByUuid(invoiceInput.getUuid(), userService.getLoggedUser().getUuid());
            invoiceEntityOldString = Utils.toJson(oldInvoice, InvoiceEntity.class);
        }
        Invoice invoice = mapper.map(invoiceInput, Invoice.class);
        InvoiceEntity invoiceEntity = mapper.map(invoice, InvoiceEntity.class);
        invoiceEntity.setSupplier(legalRepository.getOne(invoiceInput.getSupplier()));
        invoiceEntity.setCustomer(legalRepository.getOne(invoiceInput.getCustomer()));
        invoiceEntity.setUser(userService.getLoggedUser());
        try {
            invoiceEntity.setOwner(loggedUser.getUuid());
            invoiceEntity.setTotal(calculateTotal(invoiceEntity));
            InvoiceEntity save = invoiceRepository.save(invoiceEntity);
            InvoiceEntity newOne = invoiceRepository.getOne(save.getUuid());
            String invoiceEntityNewString = Utils.toJson(newOne, InvoiceEntity.class);
            log.info(MessageFormat.format("Invoice ''{0}'' was {1} by ''{2}''.",
                    invoiceInput.getName(), createdUpdated(oldInvoice), userService.getLoggedUser().getUsername()));

            auditService.log(InvoiceEntity.class, loggedUser.getUuid(), invoiceEntityOldString, invoiceEntityNewString);
            return save.getUuid();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException(MessageFormat.format("Cannot save {0}", invoiceInput.getClass().getSimpleName()));
        }
    }

    private BigDecimal calculateTotal(InvoiceEntity invoiceEntity) {
        BigDecimal result = BigDecimal.ZERO;
        for (AssetEntity asset : invoiceEntity.getAssets()) {
            result = result.add(asset.getCount().multiply(asset.getUnitPrice()));
        }
        return result;
    }

    private Invoice getInvoice(UUID uuid, UUID owner) {
        validateUuid(uuid);
        InvoiceEntity invoiceEntity = invoiceRepository.findByUuid(uuid, owner);
        if (invoiceEntity != null) {
            return mapper.map(invoiceEntity, Invoice.class);
        }
        throw new ResourceNotFoundException(Invoice.class.getSimpleName() + " not found with id " + uuid);
    }

    private Sort getSortFromInput(PagingInput input) {
        String sort = "dueDate";
        if (tableProperties.contains(input.getSort())) {
            sort = input.getSort();
        }
        return new Sort(Utils.getSortDirection(input.getSortDirection()), sort);
    }

    private void fillTableProperties() {
        tableProperties.add("name");
        tableProperties.add("customer");
        tableProperties.add("supplier");
        tableProperties.add("dueDate");
        tableProperties.add("dateOfIssue");
        tableProperties.add("deliveryDate");
    }
}

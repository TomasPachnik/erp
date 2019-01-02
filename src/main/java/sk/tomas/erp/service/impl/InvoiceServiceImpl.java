package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.bo.InvoiceInput;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.InvoiceRepository;
import sk.tomas.erp.repository.LegalRepository;
import sk.tomas.erp.service.InvoiceService;
import sk.tomas.erp.service.LegalService;
import sk.tomas.erp.service.PdfService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@MethodCallLogger
public class InvoiceServiceImpl implements InvoiceService {

    private ModelMapper mapper;
    private PdfService pdfService;
    private final UserServiceImpl userService;
    private InvoiceRepository invoiceRepository;
    private final LegalRepository legalRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public InvoiceServiceImpl(ModelMapper mapper, PdfService pdfService, InvoiceRepository invoiceRepository, UserServiceImpl userService, LegalRepository legalRepository) {
        this.mapper = mapper;
        this.pdfService = pdfService;
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.legalRepository = legalRepository;
    }

    @Override
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
    public byte[] generatePdf(UUID uuid) {
        return pdfService.generatePdf(get(uuid));
    }

    @Override
    public boolean deleteByUuid(UUID uuid) {
        try {
            invoiceRepository.deleteByUuid(uuid, userService.getLoggedUser().getUuid());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    @Override
    @Transactional
    public UUID save(InvoiceInput invoiceInput) {
        UserEntity loggedUser = userService.getLoggedUser();

        //if updating entry, check, if updater is owner
        if (invoiceInput.getUuid() != null) {
            getInvoice(invoiceInput.getUuid(), userService.getLoggedUser().getUuid());
        }
        Invoice invoice = mapper.map(invoiceInput, Invoice.class);
        InvoiceEntity invoiceEntity = mapper.map(invoice, InvoiceEntity.class);
        invoiceEntity.setSupplier(legalRepository.getOne(invoiceInput.getSupplier()));
        invoiceEntity.setCustomer(legalRepository.getOne(invoiceInput.getCustomer()));
        invoiceEntity.setUser(userService.getLoggedUser());
        try {
            invoiceEntity.setOwner(loggedUser.getUuid());
            InvoiceEntity merge = entityManager.merge(invoiceEntity);
            return merge.getUuid();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save " + invoiceInput.getClass().getSimpleName());
        }
    }

    private Invoice getInvoice(UUID uuid, UUID owner) {
        InvoiceEntity invoiceEntity = invoiceRepository.findByUuid(uuid, owner);
        if (invoiceEntity != null) {
            return mapper.map(invoiceEntity, Invoice.class);
        }
        throw new ResourceNotFoundException(Invoice.class.getSimpleName() + " not found with id " + uuid);
    }

}

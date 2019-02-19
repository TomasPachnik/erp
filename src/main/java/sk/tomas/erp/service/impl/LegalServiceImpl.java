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
import sk.tomas.erp.entity.LegalEntity;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.LegalRepository;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.service.LegalService;
import sk.tomas.erp.util.Utils;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static sk.tomas.erp.util.Utils.createdUpdated;
import static sk.tomas.erp.util.Utils.mapPaging;
import static sk.tomas.erp.validator.BaseValidator.validateUuid;
import static sk.tomas.erp.validator.InvoiceServiceValidator.validatePagingInput;
import static sk.tomas.erp.validator.LegalServiceValidator.validateLegal;

@Slf4j
@Service
@MethodCallLogger
public class LegalServiceImpl implements LegalService {

    private final UserServiceImpl userService;
    private ModelMapper mapper;
    private LegalRepository legalRepository;
    private AuditService auditService;

    @Autowired
    public LegalServiceImpl(ModelMapper mapper, LegalRepository legalRepository, UserServiceImpl userService, AuditService auditService) {
        this.mapper = mapper;
        this.legalRepository = legalRepository;
        this.userService = userService;
        this.auditService = auditService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> allCustomers() {
        return (List<Customer>) (List<?>) all(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Supplier> allSuppliers() {
        return (List<Supplier>) (List<?>) all(true);
    }

    @Override
    public Customer getCustomer(UUID uuid) {
        return (Customer) getLegal(uuid, userService.getLoggedUser().getUuid(), Customer.class);
    }

    @Override
    public Supplier getSupplier(UUID uuid) {
        return (Supplier) getLegal(uuid, userService.getLoggedUser().getUuid(), Supplier.class);
    }

    @Override
    @Transactional
    public boolean deleteCustomerByUuid(UUID uuid) {
        return deleteByUuid(uuid, false);
    }

    @Override
    @Transactional
    public boolean deleteSupplierByUuid(UUID uuid) {
        return deleteByUuid(uuid, true);
    }

    @Override
    @Transactional
    public UUID saveCustomer(Customer customer) {
        return saveLegal(customer);
    }

    @Override
    @Transactional
    public UUID saveSupplier(Supplier supplier) {
        return saveLegal(supplier);
    }

    @Override
    public Paging allSuppliers(PagingInput input) {
        return getLegal(input, Supplier.class);
    }

    @Override
    public Paging allCustomers(PagingInput input) {
        return getLegal(input, Customer.class);
    }

    private List<Legal> all(boolean supplier) {
        List<LegalEntity> all = legalRepository.all(userService.getLoggedUser().getUuid(), supplier);
        Type listType = new TypeToken<List<Legal>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    private UUID saveLegal(Legal legal) {
        validateLegal(legal);
        UserEntity loggedUser = userService.getLoggedUser();
        LegalEntity oldLegal = null;
        String legalEntityOldString = null;
        //if updating entry, check, if updater is owner
        if (legal.getUuid() != null) {
            Legal legal1 = getLegal(legal.getUuid(), userService.getLoggedUser().getUuid(), legal.getClass());
            oldLegal = legalRepository.getOne(legal1.getUuid());
            legalEntityOldString = Utils.toJson(oldLegal, LegalEntity.class);
        }
        try {
            LegalEntity legalEntity = mapper.map(legal, LegalEntity.class);
            if (legal.getClass().equals(Supplier.class)) {
                legalEntity.setSupplierFlag(true);
            } else {
                legalEntity.setSupplierFlag(false);
            }
            legalEntity.setOwner(loggedUser.getUuid());
            UUID uuid = legalRepository.save(legalEntity).getUuid();
            String legalEntityNewString = Utils.toJson(legalRepository.getOne(uuid), LegalEntity.class);
            auditService.log(LegalEntity.class, userService.getLoggedUser().getUuid(), legalEntityOldString, legalEntityNewString);

            String legalType = legalEntity.isSupplierFlag() ? "Supplier" : "Customer";
            log.info(MessageFormat.format("{0} ''{1}'' was {2} by ''{3}''.",
                    legalType, legal.getName(), createdUpdated(oldLegal), userService.getLoggedUser().getUsername()));

            return uuid;
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save " + legal.getClass().getSimpleName());
        }
    }

    private Paging getLegal(PagingInput input, Class clazz) {
        validatePagingInput(input);
        Page<LegalEntity> page;
        Type listType;
        if (clazz == Supplier.class) {
            page = legalRepository.findByOwnerAndSupplierFlag(userService.getLoggedUser().getUuid(),
                    true, PageRequest.of(input.getPageIndex(), input.getPageSize(), getSortFromInput(input)));
            listType = new TypeToken<List<Supplier>>() {
            }.getType();
        } else {
            page = legalRepository.findByOwnerAndSupplierFlag(userService.getLoggedUser().getUuid(),
                    false, PageRequest.of(input.getPageIndex(), input.getPageSize(), getSortFromInput(input)));
            listType = new TypeToken<List<Customer>>() {
            }.getType();
        }
        return mapPaging(page, mapper, listType);
    }

    private Legal getLegal(UUID uuid, UUID owner, Class clazz) {
        validateUuid(uuid);
        LegalEntity legalEntity;
        if (clazz == Supplier.class) {
            legalEntity = legalRepository.findByUuid(uuid, owner, true);
        } else {
            legalEntity = legalRepository.findByUuid(uuid, owner, false);
        }
        if (legalEntity != null) {
            return mapper.map(legalEntity, (Type) clazz);
        }
        throw new ResourceNotFoundException(clazz.getSimpleName() + " not found with id " + uuid);
    }

    private boolean deleteByUuid(UUID uuid, boolean supplier) {
        validateUuid(uuid);
        try {
            LegalEntity legalEntity = legalRepository.findByUuid(uuid, userService.getLoggedUser().getUuid(), supplier);
            if (legalEntity != null) {
                String name = legalEntity.getName();
                String legalEntityString = Utils.toJson(legalEntity, LegalEntity.class);
                auditService.log(LegalEntity.class, userService.getLoggedUser().getUuid(), legalEntityString, null);
                legalRepository.delete(legalEntity);
                String legalType = supplier ? "Supplier" : "Customer";
                log.info(MessageFormat.format("{0} ''{1}'' was deleted by ''{2}''.",
                        legalType, name, userService.getLoggedUser().getUsername()));
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Sort getSortFromInput(PagingInput input) {
        return new Sort(Utils.getSortDirection(input.getSortDirection()), "name");
    }


}

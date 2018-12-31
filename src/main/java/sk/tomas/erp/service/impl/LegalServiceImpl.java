package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.bo.Legal;
import sk.tomas.erp.bo.Supplier;
import sk.tomas.erp.entity.LegalEntity;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.repository.LegalRepository;
import sk.tomas.erp.service.LegalService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@MethodCallLogger
public class LegalServiceImpl implements LegalService {

    private ModelMapper mapper;
    private final UserServiceImpl userService;
    private LegalRepository legalRepository;

    @Autowired
    public LegalServiceImpl(ModelMapper mapper, LegalRepository legalRepository, UserServiceImpl userService) {
        this.mapper = mapper;
        this.legalRepository = legalRepository;
        this.userService = userService;
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
    public boolean deleteCustomerByUuid(UUID uuid) {
        return deleteByUuid(uuid, false);
    }

    @Override
    public boolean deleteSupplierByUuid(UUID uuid) {
        return deleteByUuid(uuid, true);
    }

    @Override
    public UUID saveCustomer(Customer customer) {
        return saveLegal(customer);
    }

    @Override
    public UUID saveSupplier(Supplier supplier) {
        return saveLegal(supplier);
    }

    private List<Legal> all(boolean supplier) {
        List<LegalEntity> all = legalRepository.all(userService.getLoggedUser().getUuid(), supplier);
        Type listType = new TypeToken<List<Legal>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    private UUID saveLegal(Legal legal) {
        UserEntity loggedUser = userService.getLoggedUser();

        //if updating entry, check, if updater is owner
        if (legal.getUuid() != null) {
            getLegal(legal.getUuid(), userService.getLoggedUser().getUuid(), legal.getClass());
        }
        try {
            LegalEntity legalEntity = mapper.map(legal, LegalEntity.class);
            if (legal.getClass().equals(Supplier.class)) {
                legalEntity.setSupplier(true);
            } else {
                legalEntity.setSupplier(false);
            }
            legalEntity.setOwner(loggedUser.getUuid());
            return legalRepository.save(legalEntity).getUuid();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save " + legal.getClass().getSimpleName());
        }
    }

    private Legal getLegal(UUID uuid, UUID owner, Class clazz) {
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
        try {
            legalRepository.deleteByUuid(uuid, userService.getLoggedUser().getUuid(), supplier);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

}

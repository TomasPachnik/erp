package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.Supplier;
import sk.tomas.erp.entity.UserEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.entity.LegalEntity;
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
    public List<Customer> allCustomers() {
        List<LegalEntity> all = legalRepository.allCustomers(userService.getLoggedUser().getUuid());
        Type listType = new TypeToken<List<Customer>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public List<Supplier> allSuppliers() {
        List<LegalEntity> all = legalRepository.allSuppliers(userService.getLoggedUser().getUuid());
        Type listType = new TypeToken<List<Supplier>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public Customer getCustomer(UUID uuid) {
        return getCustomer(uuid, userService.getLoggedUser().getUuid());
    }

    private Customer getCustomer(UUID uuid, UUID owner) {
        LegalEntity legalEntity = legalRepository.findByUuid(uuid, owner);
        if (legalEntity != null) {
            return mapper.map(legalEntity, Customer.class);
        }
        throw new ResourceNotFoundException(Customer.class.getSimpleName() + " not found with id " + uuid);
    }

    @Override
    public Supplier getSupplier(UUID uuid) {
        LegalEntity legalEntity = legalRepository.findByUuid(uuid, userService.getLoggedUser().getUuid());
        if (legalEntity != null) {
            return mapper.map(legalEntity, Supplier.class);
        }
        throw new ResourceNotFoundException(Supplier.class.getSimpleName() + " not found with id " + uuid);
    }

    @Override
    public boolean delete(UUID uuid) {
        try {
            legalRepository.deleteByUuid(uuid, userService.getLoggedUser().getUuid());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public UUID saveCustomer(Customer customer) {
        UserEntity loggedUser = userService.getLoggedUser();
        getCustomer(customer.getUuid(), userService.getLoggedUser().getUuid());
        try {
            LegalEntity legal = mapper.map(customer, LegalEntity.class);
            legal.setSupplier(false);
            legal.setOwner(loggedUser.getUuid());
            return legalRepository.save(legal).getUuid();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save customer");
        }
    }

}

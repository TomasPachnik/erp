package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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
public class LegalServiceImpl implements LegalService {

    private ModelMapper mapper;
    private LegalRepository legalRepository;

    @Autowired
    public LegalServiceImpl(ModelMapper mapper, LegalRepository legalRepository) {
        this.mapper = mapper;
        this.legalRepository = legalRepository;
    }

    @Override
    public List<Customer> allCustomers() {
        List<LegalEntity> all = legalRepository.allCustomers();
        Type listType = new TypeToken<List<Customer>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public List<Supplier> allSuppliers() {
        List<LegalEntity> all = legalRepository.allSuppliers();
        Type listType = new TypeToken<List<Supplier>>() {
        }.getType();
        return mapper.map(all, listType);
    }

    @Override
    public Customer getCustomer(UUID uuid) {
        return legalRepository.findById(uuid)
                .map(legalEntity -> mapper.map(legalEntity, Customer.class))
                .orElseThrow(() -> new ResourceNotFoundException(Customer.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    public Supplier getSupplier(UUID uuid) {
        return legalRepository.findById(uuid)
                .map(legalEntity -> mapper.map(legalEntity, Supplier.class))
                .orElseThrow(() -> new ResourceNotFoundException(Supplier.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public UUID saveCustomer(Customer customer) {
        try {
            LegalEntity legal = mapper.map(customer, LegalEntity.class);
            legal.setSupplier(false);
            return legalRepository.save(legal).getUuid();
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new SqlException("Cannot save customer");
        }
    }

}

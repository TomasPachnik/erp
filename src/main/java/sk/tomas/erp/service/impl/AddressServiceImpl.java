package sk.tomas.erp.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.Address;
import sk.tomas.erp.entity.AddressEntity;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.repository.AddressRepository;
import sk.tomas.erp.service.AddressService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static sk.tomas.erp.validator.BaseValidator.validateUuid;

@Service
@MethodCallLogger
public class AddressServiceImpl implements AddressService {

    private ModelMapper mapper;
    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(ModelMapper mapper, AddressRepository addressRepository) {
        this.mapper = mapper;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address get(UUID uuid) {
        validateUuid(uuid);
        return addressRepository.findById(uuid)
                .map(addressEntity -> mapper.map(addressEntity, Address.class))
                .orElseThrow(() -> new ResourceNotFoundException(Address.class.getSimpleName() + " not found with id " + uuid));
    }

    @Override
    public List<Address> all() {
        List<AddressEntity> all = addressRepository.findAll();
        Type listType = new TypeToken<List<Address>>() {
        }.getType();
        return mapper.map(all, listType);
    }
}

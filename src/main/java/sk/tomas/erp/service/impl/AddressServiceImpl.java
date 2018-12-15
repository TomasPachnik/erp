package sk.tomas.erp.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tomas.erp.ResourceNotFoundException;
import sk.tomas.erp.bo.Address;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.entity.AddressEntity;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.repository.AddressRepository;
import sk.tomas.erp.service.AddressService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public Address get(UUID uuid) {
        return addressRepository.findById(uuid)
                .map(invoiceEntity -> mapper.map(addressRepository, Address.class))
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id " + uuid));
    }

    @Override
    public List<Address> all() {
        List<AddressEntity> all = addressRepository.findAll();
        Type listType = new TypeToken<List<Address>>() {
        }.getType();
        return mapper.map(all, listType);
    }
}

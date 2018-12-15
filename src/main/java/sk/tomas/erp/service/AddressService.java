package sk.tomas.erp.service;

import sk.tomas.erp.bo.Address;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    Address get(UUID uuid);

    List<Address> all();
}

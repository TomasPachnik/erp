package sk.tomas.erp.service;

import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.bo.Supplier;

import java.util.List;
import java.util.UUID;

public interface LegalService {

    List<Customer> allCustomers();

    List<Supplier> allSuppliers();

    Customer getCustomer(UUID uuid);

    Supplier getSupplier(UUID uuid);

    boolean delete(UUID uuid);

    UUID saveCustomer(Customer customer);
}

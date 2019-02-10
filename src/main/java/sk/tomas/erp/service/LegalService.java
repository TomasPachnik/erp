package sk.tomas.erp.service;

import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.bo.Paging;
import sk.tomas.erp.bo.PagingInput;
import sk.tomas.erp.bo.Supplier;

import java.util.List;
import java.util.UUID;

public interface LegalService {

    List<Customer> allCustomers();

    List<Supplier> allSuppliers();

    Customer getCustomer(UUID uuid);

    Supplier getSupplier(UUID uuid);

    boolean deleteCustomerByUuid(UUID uuid);

    boolean deleteSupplierByUuid(UUID uuid);

    UUID saveCustomer(Customer customer);

    UUID saveSupplier(Supplier supplier);

    Paging allSuppliers(PagingInput input);

    Paging allCustomers(PagingInput input);
}

package sk.tomas.erp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.bo.*;
import sk.tomas.erp.service.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PositiveFlowTest {

    @Autowired
    private UserService userService;
    @Autowired
    private LegalService legalService;
    @Autowired
    private DateService dateService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private ModelMapper mapper;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void positiveFlowTest() {
        updateUser();
        changePassword();
        UUID user = createUserByAdmin();
        updateUserByAdmin(user);
        UUID supplier = createSupplier();
        updateSupplier(supplier);
        UUID customer = createCustomer();
        updateCustomer(customer);
        UUID invoice = createInvoice(supplier, customer);
        pdfService.generatePdf(invoiceService.get(invoice));
        updateInvoice(invoice);

        userService.all();
        legalService.allCustomers();
        legalService.allSuppliers();
        invoiceService.all();

        userService.allUsers(new PagingInput(null, null, null, 0, 10));
        legalService.allCustomers(new PagingInput(null, null, null, 0, 10));
        legalService.allSuppliers(new PagingInput(null, null, null, 0, 10));
        invoiceService.allInvoices(new PagingInput(null, null, null, 0, 10));

        deleteInvoice(invoice);
        deleteSupplier(supplier);
        deleteCustomer(customer);
        deleteUserByAdmin(user);
    }

    private void updateUser() {
        User user = userService.getByToken();
        user.setPhone("phone number");
        ChangeUser changeUser = new ChangeUser();
        changeUser.setEmail("new@new");
        changeUser.setName("User user");
        changeUser.setPhone("new_phone");
        userService.saveCurrent(changeUser);
        User changed = userService.getByToken();
        Assert.assertEquals(changeUser.getName(), changed.getName());
        Assert.assertEquals(changeUser.getEmail(), changed.getEmail());
        Assert.assertEquals(changeUser.getPhone(), changed.getPhone());
    }

    private void changePassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("password");
        changePassword.setNewPassword("Password1");
        userService.changePassword(changePassword);
    }

    private UUID createUserByAdmin() {
        User user = new User();
        user.setUsername("login");
        user.setName("name");
        user.setEmail("email@email");
        user.setPhone("phone");
        UUID uuid = userService.save(user);
        User changed = userService.get(uuid);
        Assert.assertEquals(user, changed);
        return uuid;
    }

    private void updateUserByAdmin(UUID uuid) {
        User user = userService.get(uuid);
        user.setPhone("new_phone");
        userService.save(user);
        User changed = userService.get(uuid);
        Assert.assertEquals(user.getPhone(), changed.getPhone());
    }

    private UUID createSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("name");
        supplier.setCompanyIdentificationNumber("ico");
        supplier.setTaxIdentificationNumber("dic");
        supplier.setBankAccount(createAccount());
        supplier.setAddress(createAddress());
        UUID uuid = legalService.saveSupplier(supplier);
        Supplier changed = legalService.getSupplier(uuid);
        Assert.assertEquals(supplier, changed);
        return uuid;
    }

    private void updateSupplier(UUID uuid) {
        Supplier supplier = legalService.getSupplier(uuid);
        supplier.setName("new_name");
        legalService.saveSupplier(supplier);
        Supplier changed = legalService.getSupplier(uuid);
        Assert.assertEquals(supplier.getName(), changed.getName());
    }

    private UUID createCustomer() {
        Customer customer = new Customer();
        customer.setName("name");
        customer.setCompanyIdentificationNumber("ico");
        customer.setTaxIdentificationNumber("dic");
        customer.setBankAccount(createAccount());
        customer.setAddress(createAddress());
        UUID uuid = legalService.saveCustomer(customer);
        Customer changed = legalService.getCustomer(uuid);
        Assert.assertEquals(customer, changed);
        return uuid;
    }

    private void updateCustomer(UUID uuid) {
        Customer customer = legalService.getCustomer(uuid);
        customer.setName("new_name");
        legalService.saveCustomer(customer);
        Customer changed = legalService.getCustomer(uuid);
        Assert.assertEquals(customer.getName(), changed.getName());
    }

    private UUID createInvoice(UUID supplier, UUID customer) {
        InvoiceInput invoice = new InvoiceInput();
        invoice.setName("name");
        invoice.setInvoiceNumber("invoice_number");
        invoice.setCurrency("€");
        invoice.setSupplier(supplier);
        invoice.setCustomer(customer);
        invoice.setSupplierVariableSymbol("variable_symbol");
        invoice.setDateOfIssue(dateService.getActualDate());
        invoice.setDeliveryDate(dateService.getActualDate());
        invoice.setDueDate(dateService.getActualDate());
        invoice.setNote("note");
        invoice.setAssets(createAssets());
        UUID uuid = invoiceService.save(invoice);
        Invoice changed = invoiceService.get(uuid);
        InvoiceInput invoiceInput = mapper.map(changed, InvoiceInput.class);
        invoiceInput.setSupplier(changed.getSupplier().getUuid());
        invoiceInput.setCustomer(changed.getCustomer().getUuid());
        invoiceInput.setDateOfIssue(new Date(invoiceInput.getDateOfIssue().getTime()));
        invoiceInput.setDeliveryDate(new Date(invoiceInput.getDeliveryDate().getTime()));
        invoiceInput.setDueDate(new Date(invoiceInput.getDueDate().getTime()));
        Assert.assertEquals(invoice, invoiceInput);
        return uuid;
    }

    private void updateInvoice(UUID uuid) {
        Invoice invoice = invoiceService.get(uuid);
        invoice.setName("new_name");
        InvoiceInput invoiceInput = mapper.map(invoice, InvoiceInput.class);
        invoiceInput.setSupplier(invoice.getSupplier().getUuid());
        invoiceInput.setCustomer(invoice.getCustomer().getUuid());
        invoiceService.save(invoiceInput);
        Invoice changed = invoiceService.get(uuid);
        invoice.setDateOfIssue(new Date(changed.getDateOfIssue().getTime()));
        invoice.setDeliveryDate(new Date(changed.getDeliveryDate().getTime()));
        invoice.setDueDate(new Date(changed.getDueDate().getTime()));
        Assert.assertEquals(invoice, changed);
    }

    private void deleteInvoice(UUID uuid) {
        boolean deleted1 = invoiceService.deleteByUuid(uuid);
        boolean deleted2 = invoiceService.deleteByUuid(uuid);
        Assert.assertTrue(deleted1);
        Assert.assertFalse(deleted2);
    }

    private void deleteSupplier(UUID uuid) {
        boolean deleted1 = legalService.deleteSupplierByUuid(uuid);
        boolean deleted2 = legalService.deleteSupplierByUuid(uuid);
        Assert.assertTrue(deleted1);
        Assert.assertFalse(deleted2);
    }

    private void deleteCustomer(UUID uuid) {
        boolean deleted1 = legalService.deleteCustomerByUuid(uuid);
        boolean deleted2 = legalService.deleteCustomerByUuid(uuid);
        Assert.assertTrue(deleted1);
        Assert.assertFalse(deleted2);
    }

    private void deleteUserByAdmin(UUID uuid) {
        boolean deleted1 = userService.delete(uuid);
        boolean deleted2 = userService.delete(uuid);
        Assert.assertTrue(deleted1);
        Assert.assertFalse(deleted2);
    }

    private BankAccount createAccount() {
        BankAccount account = new BankAccount();
        account.setBankName("bank_name");
        account.setIban("FR7630006000011234567890189");
        return account;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setStreet("street");
        address.setHouseNumber("house_number");
        address.setPostalCode("postal");
        address.setTown("town");
        address.setCountry("country");
        return address;
    }

    private List<Asset> createAssets() {
        Asset asset1 = new Asset();
        asset1.setName("asset1");
        asset1.setCount(new BigDecimal("4.00"));
        asset1.setUnit("unit1");
        asset1.setUnitPrice(new BigDecimal("12.40"));

        Asset asset2 = new Asset();
        asset2.setName("asset2");
        asset2.setCount(new BigDecimal("13.00"));
        asset2.setUnit("unit2");
        asset2.setUnitPrice(new BigDecimal("7.50"));
        return Arrays.asList(asset1, asset2);
    }
}

package sk.tomas.erp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.bo.PagingInput;
import sk.tomas.erp.service.PdfService;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PositiveFlowTest extends BaseTest{

    @Autowired
    private PdfService pdfService;

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
        UUID invoice = createInvoice(supplier, customer, dateService.getActualDate());
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
}

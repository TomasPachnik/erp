package sk.tomas.erp;

import net.sf.jasperreports.engine.JRException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.bo.*;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.service.InvoiceService;
import sk.tomas.erp.service.LegalService;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErpApplicationTests {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LegalService legalService;

    @Test
    public void contextLoads() throws IOException, JRException {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setCurrency("nejaka");
        //File nazov = invoiceService.generatePdf(invoice);
        System.out.println();
    }


    @Test
    public void passwordTest() {
        String password = passwordEncoder.encode("password");
        boolean matches = passwordEncoder.matches("password", "{bcrypt}$2a$10$C4iT56OLDdKD8b7deaAqDeAcGy9ekAAE7nd1K4iJ9qSf0Y7Y6y8q2");
        boolean matches2 = passwordEncoder.matches("password", "{bcrypt}$2a$10$LneahdWVNIojHDLcMfv93eU32dfLII.hoR/39BGEu47Z.r4vpnWty");
        System.out.println();
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void invoiceSaveTest() {
        Supplier supplier = new Supplier();
        supplier.setName("supp");

        Customer customer = new Customer();
        customer.setName("cust");

        UUID supplierUuid = legalService.saveSupplier(supplier);
        UUID customerUuid = legalService.saveCustomer(customer);

        InvoiceInput input = new InvoiceInput();
        input.setSupplier(supplierUuid);
        input.setCustomer(customerUuid);

        invoiceService.save(input);

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void paginationTest(){
        Page all = invoiceService.all(new PagingInput(0, 1));
        System.out.println(all.getContent().size());
    }

}

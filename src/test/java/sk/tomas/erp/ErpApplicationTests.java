package sk.tomas.erp;

import net.sf.jasperreports.engine.JRException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.service.InvoiceService;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErpApplicationTests {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() throws IOException, JRException {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setCurrency("nejaka");
        //File nazov = invoiceService.generatePdf(invoice);
        System.out.println();
    }


    @Test
    public void passwordTest(){
        String password = passwordEncoder.encode("password");
        boolean matches = passwordEncoder.matches("password", "{bcrypt}$2a$10$C4iT56OLDdKD8b7deaAqDeAcGy9ekAAE7nd1K4iJ9qSf0Y7Y6y8q2");
        boolean matches2 = passwordEncoder.matches("password", "{bcrypt}$2a$10$LneahdWVNIojHDLcMfv93eU32dfLII.hoR/39BGEu47Z.r4vpnWty");
        System.out.println();
    }

}

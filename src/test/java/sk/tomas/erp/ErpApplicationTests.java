package sk.tomas.erp;

import net.sf.jasperreports.engine.JRException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.dto.Invoice;
import sk.tomas.erp.service.InvoiceService;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErpApplicationTests {

	@Autowired
	private InvoiceService invoiceService;

	@Test
	public void contextLoads() throws IOException, JRException {
		Invoice invoice = new Invoice();
		invoice.setCurrency("nejaka");
		File nazov = invoiceService.generatePdf(invoice, "nazov");
		System.out.println();
	}

}

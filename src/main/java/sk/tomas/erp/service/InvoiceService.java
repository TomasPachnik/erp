package sk.tomas.erp.service;

import net.sf.jasperreports.engine.JRException;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.bo.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    File generatePdf(Invoice invoice, String name) throws JRException, IOException;

}

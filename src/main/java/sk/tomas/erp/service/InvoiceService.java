package sk.tomas.erp.service;

import net.sf.jasperreports.engine.JRException;
import sk.tomas.erp.dto.Invoice;

import java.io.File;
import java.io.IOException;

public interface InvoiceService {

    File generatePdf(Invoice invoice, String name) throws JRException, IOException;

}

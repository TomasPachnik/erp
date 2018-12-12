package sk.tomas.erp.service;

import net.sf.jasperreports.engine.JRException;
import sk.tomas.erp.bo.Invoice;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface InvoiceService {

    Invoice getById(UUID uuid);

    File generatePdf(Invoice invoice,String name) throws JRException, IOException;

}

package sk.tomas.erp.service;

import net.sf.jasperreports.engine.JRException;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.entity.InvoiceEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    Invoice get(UUID uuid);

    File generatePdf(InvoiceEntity invoice, String name) throws JRException, IOException;

    Invoice generate();

    List<Invoice> all();
}

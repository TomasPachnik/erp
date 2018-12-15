package sk.tomas.erp.service;

import sk.tomas.erp.bo.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    Invoice get(UUID uuid);

    Invoice generate();

    List<Invoice> all();

    byte[] generatePdf(UUID uuid);
}

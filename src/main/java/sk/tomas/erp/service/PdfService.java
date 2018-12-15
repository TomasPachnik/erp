package sk.tomas.erp.service;

import sk.tomas.erp.bo.Invoice;

public interface PdfService {

    byte[] generatePdf(Invoice invoice);

}

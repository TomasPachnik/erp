package sk.tomas.erp.service;

import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.bo.InvoiceInput;
import sk.tomas.erp.bo.Paging;
import sk.tomas.erp.bo.PagingInput;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    Invoice get(UUID uuid);

    List<Invoice> all();

    Paging all(PagingInput input);

    boolean deleteByUuid(UUID uuid);

    UUID save(InvoiceInput invoiceInput);
}

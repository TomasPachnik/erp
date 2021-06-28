package sk.tomas.erp.service;

import sk.tomas.erp.bo.*;
import sk.tomas.erp.entity.Last12Months;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    Invoice get(UUID uuid);

    List<Invoice> all();

    Paging allInvoices(PagingInput input);

    boolean deleteByUuid(UUID uuid);

    UUID save(InvoiceInput invoiceInput, boolean quick);

    UUID saveQuickInvoice(QuickInvoiceInput invoice);

    Last12Months calculateRevenueForLast12Months();
}

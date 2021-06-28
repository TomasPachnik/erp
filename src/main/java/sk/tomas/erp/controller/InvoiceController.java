package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.*;
import sk.tomas.erp.entity.Last12Months;
import sk.tomas.erp.service.InvoiceService;
import sk.tomas.erp.service.PdfService;

import java.util.UUID;

@RestController
@RequestMapping("/invoices")
@PreAuthorize("hasRole('ROLE_USER')")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfService pdfService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, PdfService pdfService) {
        this.invoiceService = invoiceService;
        this.pdfService = pdfService;
    }

    @GetMapping("/get/{uuid}")
    public Invoice get(@PathVariable UUID uuid) {
        return invoiceService.get(uuid);
    }

    @PostMapping(path = "/all")
    public Paging all(@RequestBody PagingInput input) {
        return invoiceService.allInvoices(input);
    }

    @GetMapping(path = "/last12Months")
    public Last12Months last12Months() {
        return invoiceService.calculateRevenueForLast12Months();
    }

    @GetMapping("/remove/{uuid}")
    public boolean delete(@PathVariable UUID uuid) {
        return invoiceService.deleteByUuid(uuid);
    }

    @PostMapping(path = "/save")
    public UUID save(@RequestBody InvoiceInput invoice) {
        return invoiceService.save(invoice, false);
    }

    @PostMapping(path = "/saveQuickInvoice")
    public UUID saveQuickInvoice(@RequestBody QuickInvoiceInput invoice) {
        return invoiceService.saveQuickInvoice(invoice);
    }

    @GetMapping(path = "/generate/{uuid}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateInvoice(@PathVariable UUID uuid) {
        Invoice invoice = get(uuid);
        byte[] generatedPdf = pdfService.generatePdf(invoice);
        return new ResponseEntity<>(generatedPdf, generatePdfHeaders(invoice.getInvoiceNumber()), HttpStatus.OK);
    }

    private HttpHeaders generatePdfHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        fileName = fileName + ".pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

}

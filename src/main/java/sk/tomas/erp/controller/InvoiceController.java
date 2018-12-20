package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.service.InvoiceService;
import sk.tomas.erp.service.PdfService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfService pdfService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, PdfService pdfService) {
        this.invoiceService = invoiceService;
        this.pdfService = pdfService;
    }

    @GetMapping("/{uuid}")
    public Invoice get(@PathVariable UUID uuid) {
        return invoiceService.get(uuid);
    }

    @GetMapping("/")
    public List<Invoice> all() {
        return invoiceService.all();
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

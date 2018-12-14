package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.dto.Invoice;
import sk.tomas.erp.repository.InvoiceRepository;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/save")
    public String save() {
        Invoice invoice = new Invoice();
        invoice.setCurrency("€");
        invoiceRepository.save(invoice);
        return "hello";
    }

    @GetMapping("/get")
    public List<Invoice> get() {
        return invoiceRepository.findAll();
    }

}

package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.service.InvoiceService;

import java.util.UUID;

@RestController
@RequestMapping("/invoices")
public class HelloController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{uuid}")
    public Invoice get(@PathVariable UUID uuid) {
        return invoiceService.get(uuid);
    }

}

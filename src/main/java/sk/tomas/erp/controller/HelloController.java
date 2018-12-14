package sk.tomas.erp.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.repository.InvoiceRepository;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/save")
    public String save() {
        Invoice invoice = new Invoice();
        invoice.setCurrency("€");
        invoiceRepository.save(mapper.map(invoice, sk.tomas.erp.dto.Invoice.class));
        return "hello";
    }

    @GetMapping("/get")
    public List<Invoice> get() {
        java.lang.reflect.Type targetListType = new TypeToken<List<Invoice>>() {
        }.getType();
        return mapper.map(invoiceRepository.findAll(), targetListType);
    }


}

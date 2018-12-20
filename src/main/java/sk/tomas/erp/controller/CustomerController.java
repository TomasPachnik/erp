package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.service.LegalService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final LegalService legalService;

    @Autowired
    public CustomerController(LegalService legalService) {
        this.legalService = legalService;
    }

    @GetMapping("/")
    public List<Customer> all() {
        return legalService.allCustomers();
    }

    @GetMapping("/{uuid}")
    public Customer get(@PathVariable UUID uuid) {
        return legalService.getCustomer(uuid);
    }

    @GetMapping("/delete/{uuid}")
    public boolean delete(@PathVariable UUID uuid) {
        return legalService.delete(uuid);
    }

    @PostMapping(path = "/save")
    public UUID save(@Valid @RequestBody Customer customer) {
        return legalService.saveCustomer(customer);
    }

}

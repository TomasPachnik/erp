package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.bo.Paging;
import sk.tomas.erp.bo.PagingInput;
import sk.tomas.erp.service.LegalService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@PreAuthorize("hasRole('ROLE_USER')")
public class CustomerController {

    private final LegalService legalService;

    @Autowired
    public CustomerController(LegalService legalService) {
        this.legalService = legalService;
    }

    @GetMapping("/all")
    public List<Customer> all() {
        return legalService.allCustomers();
    }

    @PostMapping("/all")
    public Paging allWithPagination(@RequestBody PagingInput input) {
        return legalService.allCustomers(input);
    }

    @GetMapping("/get/{uuid}")
    public Customer get(@PathVariable UUID uuid) {
        return legalService.getCustomer(uuid);
    }

    @GetMapping("/remove/{uuid}")
    public boolean delete(@PathVariable UUID uuid) {
        return legalService.deleteCustomerByUuid(uuid);
    }

    @PostMapping(path = "/save")
    public UUID save(@Valid @RequestBody Customer customer) {
        return legalService.saveCustomer(customer);
    }

}

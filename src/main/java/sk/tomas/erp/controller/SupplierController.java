package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.Paging;
import sk.tomas.erp.bo.PagingInput;
import sk.tomas.erp.bo.Supplier;
import sk.tomas.erp.service.LegalService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/suppliers")
@PreAuthorize("hasRole('ROLE_USER')")
public class SupplierController {

    private final LegalService legalService;

    @Autowired
    public SupplierController(LegalService legalService) {
        this.legalService = legalService;
    }

    @GetMapping("/all")
    public List<Supplier> all() {
        return legalService.allSuppliers();
    }

    @PostMapping("/all")
    public Paging allWithPagination(@RequestBody PagingInput input) {
        return legalService.allSuppliers(input);
    }

    @GetMapping("/get/{uuid}")
    public Supplier get(@PathVariable UUID uuid) {
        return legalService.getSupplier(uuid);
    }

    @GetMapping("/remove/{uuid}")
    public boolean delete(@PathVariable UUID uuid) {
        return legalService.deleteSupplierByUuid(uuid);
    }

    @PostMapping(path = "/save")
    public UUID save(@Valid @RequestBody Supplier supplier) {
        return legalService.saveSupplier(supplier);
    }

}

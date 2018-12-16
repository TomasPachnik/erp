package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.bo.Legal;
import sk.tomas.erp.service.LegalService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/legals")
public class LegalController {

    private final LegalService legalService;

    @Autowired
    public LegalController(LegalService legalService) {
        this.legalService = legalService;
    }

    @GetMapping("/{uuid}")
    public Legal get(@PathVariable UUID uuid) {
        return legalService.get(uuid);
    }

    @GetMapping("/")
    public List<Legal> all() {
        return legalService.all();
    }

}

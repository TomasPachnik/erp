package sk.tomas.erp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.StringInput;
import sk.tomas.erp.service.AuditService;

@Slf4j
@RestController
@RequestMapping("/audit")
public class AuditController {

    private AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/backup")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result backup() {
        auditService.backup();
        return new Result(true);
    }

    @GetMapping("/restore")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result restore() {
        auditService.restore();
        return new Result(true);
    }

    @PostMapping(path = "/auditEmail")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result sendAuditData(@RequestBody StringInput input) {
        return auditService.sendAuditData(input);
    }

}

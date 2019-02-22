package sk.tomas.erp.service;

import sk.tomas.erp.entity.AuditEntity;

import java.util.List;

public interface EmailService {

    void sendAuditData(String toEmailAddress, List<AuditEntity> auditEntities);

}

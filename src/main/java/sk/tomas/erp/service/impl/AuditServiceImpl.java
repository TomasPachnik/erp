package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.entity.AuditEntity;
import sk.tomas.erp.repository.AuditRepository;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.service.DateService;

import java.util.UUID;

@Slf4j
@Service
@MethodCallLogger
public class AuditServiceImpl implements AuditService {

    private AuditRepository auditRepository;
    private DateService dateService;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository, DateService dateService) {
        this.auditRepository = auditRepository;
        this.dateService = dateService;
    }

    @Override
    public void log(Class clazz, UUID owner, String oldValue, String newValue) {
        AuditEntity entity = new AuditEntity();
        entity.setOwner(owner);
        entity.setClassName(clazz.getName());
        entity.setDate(dateService.getActualDate());
        entity.setOldValue(oldValue);
        entity.setNewValue(newValue);
        auditRepository.save(entity);
    }

}

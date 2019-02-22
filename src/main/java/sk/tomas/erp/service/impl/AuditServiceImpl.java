package sk.tomas.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sk.tomas.erp.annotations.MethodCallLogger;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.StringInput;
import sk.tomas.erp.entity.*;
import sk.tomas.erp.repository.*;
import sk.tomas.erp.service.AuditService;
import sk.tomas.erp.service.DateService;
import sk.tomas.erp.service.EmailService;
import sk.tomas.erp.util.Utils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static sk.tomas.erp.util.Utils.fromJson;
import static sk.tomas.erp.validator.AuditValidator.validateStringInput;

@Slf4j
@Service
@MethodCallLogger
public class AuditServiceImpl implements AuditService {

    private AuditRepository auditRepository;
    private DateService dateService;
    private EmailService emailService;
    private InvoiceRepository invoiceRepository;
    private LegalRepository legalRepository;
    private UserRepository userRepository;
    private GenericRepository genericRepository;


    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository, DateService dateService, EmailService emailService,
                            InvoiceRepository invoiceRepository, LegalRepository legalRepository,
                            UserRepository userRepository, GenericRepository genericRepository) {
        this.auditRepository = auditRepository;
        this.dateService = dateService;
        this.emailService = emailService;
        this.invoiceRepository = invoiceRepository;
        this.legalRepository = legalRepository;
        this.userRepository = userRepository;
        this.genericRepository = genericRepository;
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

    @Override
    @Transactional
    public void backup() {
        auditRepository.deleteAll();
        auditRepository.flush();
        userRepository.findAll().forEach(entity ->
                log(UserEntity.class, entity.getUuid(), null, Utils.toJson(entity, UserEntity.class)));
        legalRepository.findAll().forEach(entity ->
                log(LegalEntity.class, entity.getOwner(), null, Utils.toJson(entity, LegalEntity.class)));
        invoiceRepository.findAll().forEach(entity ->
                log(InvoiceEntity.class, entity.getOwner(), null, Utils.toJson(entity, InvoiceEntity.class)));
        log.info("Successfully performed data backup to audit.");
    }

    @Override
    @Transactional
    public void restore() {
        deleteAllDataExceptAudit();
        auditRepository.findAll(new Sort(Sort.Direction.ASC, "date")).forEach(this::restoreItemFromAudit);
        log.info("Successfully performed data reload from audit.");
    }

    @Override
    public List<AuditEntity> all() {
        return auditRepository.findAll();
    }

    @Override
    public Result sendAuditData(StringInput input) {
        validateStringInput(input);
        emailService.sendAuditData(input.getValue(), all());
        return new Result(true);
    }


    private void deleteAllDataExceptAudit() {
        invoiceRepository.deleteAll();
        invoiceRepository.flush();
        legalRepository.deleteAll();
        legalRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @SuppressWarnings("unchecked")
    private void restoreItemFromAudit(AuditEntity entity) {
        Class<?> clazz;
        try {
            clazz = Class.forName(entity.getClassName());
        } catch (ClassNotFoundException e) {
            log.error("error restoring data on entry {0}", entity.getUuid());
            return;
        }
        JpaRepository repository = genericRepository.getRepository(clazz);

        BaseEntity oldObject = (BaseEntity) fromJson(entity.getOldValue(), clazz);
        BaseEntity newObject = (BaseEntity) fromJson(entity.getNewValue(), clazz);

        if (entity.getOldValue() == null && entity.getNewValue() != null) {
            repository.save(newObject);
        } else {
            if (entity.getOldValue() != null && entity.getNewValue() != null) {
                repository.save(newObject);
            } else {
                if (entity.getOldValue() != null && entity.getNewValue() == null) {
                    repository.delete(oldObject);
                    repository.flush();
                }
            }
        }
    }
}

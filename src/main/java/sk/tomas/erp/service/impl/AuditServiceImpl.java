package sk.tomas.erp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper mapper;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository, DateService dateService) {
        mapper = new ObjectMapper();
        this.auditRepository = auditRepository;
        this.dateService = dateService;
    }

    @Override
    public void log(Class clazz, UUID owner, Object oldValue, Object newValue) {
        AuditEntity entity = new AuditEntity();
        entity.setOwner(owner);
        entity.setClassName(clazz.getName());
        entity.setDate(dateService.getActualDate());
        entity.setOldValue(convert(oldValue, clazz));
        entity.setNewValue(convert(newValue, clazz));

        auditRepository.save(entity);
    }

    private String convert(Object object, Class clazz) {
        if (object != null) {
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.warn("Cant convert object " + clazz.getName() + " to JSON", e);
            }
        }
        return null;
    }

}

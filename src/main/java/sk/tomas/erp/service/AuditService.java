package sk.tomas.erp.service;

import sk.tomas.erp.entity.BaseEntity;

import java.util.UUID;

public interface AuditService {

    void log(Class<? extends BaseEntity> clazz, UUID owner, String oldValue, String newValue);

    void restore();

}

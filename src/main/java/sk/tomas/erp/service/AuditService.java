package sk.tomas.erp.service;

import java.util.UUID;

public interface AuditService {

    void log(Class clazz, UUID owner, Object oldValue, Object newValue);

}

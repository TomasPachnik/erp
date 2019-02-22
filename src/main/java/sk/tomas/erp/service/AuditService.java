package sk.tomas.erp.service;

import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.StringInput;
import sk.tomas.erp.entity.AuditEntity;
import sk.tomas.erp.entity.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface AuditService {

    void log(Class<? extends BaseEntity> clazz, UUID owner, String oldValue, String newValue);

    void backup();

    void restore();

    List<AuditEntity> all();

    Result sendAuditData(StringInput input);
}

package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.entity.AuditEntity;

import java.util.UUID;

public interface AuditRepository extends JpaRepository<AuditEntity, UUID> {

}

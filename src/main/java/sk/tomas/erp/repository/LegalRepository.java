package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.entity.LegalEntity;

import java.util.UUID;

public interface LegalRepository extends JpaRepository<LegalEntity, UUID> {
}

package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.tomas.erp.entity.LegalEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface LegalRepository extends JpaRepository<LegalEntity, UUID> {

    @Query("SELECT l FROM LegalEntity l WHERE l.owner = ?1 and (l.supplierFlag) = ?2")
    List<LegalEntity> all(UUID owner, boolean supplier);

    @Query("SELECT l FROM LegalEntity l WHERE (l.uuid) = ?1 and l.owner = ?2 and (l.supplierFlag) = ?3")
    LegalEntity findByUuid(UUID uuid, UUID owner, boolean supplier);

}

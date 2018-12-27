package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.tomas.erp.entity.LegalEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface LegalRepository extends JpaRepository<LegalEntity, UUID> {

    @Query("SELECT l FROM LegalEntity l WHERE (l.supplier) = false and l.owner = ?1")
    List<LegalEntity> allCustomers(UUID owner);

    @Query("SELECT l FROM LegalEntity l WHERE (l.supplier) = true and l.owner = ?1")
    List<LegalEntity> allSuppliers(UUID owner);

    @Query("SELECT l FROM LegalEntity l WHERE (l.uuid) = ?1 and l.owner = ?2")
    LegalEntity findByUuid(UUID uuid, UUID owner);

    @Modifying
    @Transactional
    @Query("DELETE FROM LegalEntity l WHERE (l.uuid) = ?1 and l.owner = ?2")
    void deleteByUuid(UUID uuid, UUID owner);

}

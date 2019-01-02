package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.tomas.erp.entity.InvoiceEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {

    @Query("SELECT l FROM InvoiceEntity l WHERE l.owner = ?1")
    List<InvoiceEntity> all(UUID owner);

    @Query("SELECT l FROM InvoiceEntity l WHERE (l.uuid) = ?1 and l.owner = ?2")
    InvoiceEntity findByUuid(UUID uuid, UUID owner);

    @Modifying
    @Transactional
    @Query("DELETE FROM InvoiceEntity l WHERE  (l.uuid) = ?1 and l.owner = ?2   ")
    void deleteByUuid(UUID uuid, UUID owner);

}

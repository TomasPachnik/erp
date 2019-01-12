package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.tomas.erp.entity.AssetEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AssetEntity l WHERE  (l.uuid) in ?1")
    void deleteByUuid(List<UUID> uuids);

}

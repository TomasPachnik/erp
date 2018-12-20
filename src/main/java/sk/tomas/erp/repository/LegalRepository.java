package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.tomas.erp.entity.LegalEntity;
import sk.tomas.erp.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface LegalRepository extends JpaRepository<LegalEntity, UUID> {

    @Query("SELECT l FROM LegalEntity l WHERE (l.supplier) = false")
    List<LegalEntity> allCustomers();

    @Query("SELECT l FROM LegalEntity l WHERE (l.supplier) = true")
    List<LegalEntity> allSuppliers();

}

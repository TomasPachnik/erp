package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.entity.AddressEntity;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
}

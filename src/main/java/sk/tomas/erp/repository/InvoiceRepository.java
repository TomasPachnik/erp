package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.entity.InvoiceEntity;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
}

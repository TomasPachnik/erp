package sk.tomas.erp.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "legal_entity")
public class LegalEntity extends BaseEntity {

    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;

}

package sk.tomas.erp.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "legal_entity")
public class LegalEntity extends BaseEntity {

    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;

}

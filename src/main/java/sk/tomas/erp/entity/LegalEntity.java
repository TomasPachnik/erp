package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "legal")
public class LegalEntity extends OwnerEntity {

    private boolean supplier;
    private String name;
    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private AddressEntity address;

}

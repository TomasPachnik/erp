package sk.tomas.erp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "legal")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class LegalEntity extends OwnerEntity {

    private boolean supplierFlag;
    private String name;
    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private BankAccountEntity bankAccount;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private AddressEntity address;

}

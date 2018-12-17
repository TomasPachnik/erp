package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoice")
@Inheritance(strategy = InheritanceType.JOINED)
public class InvoiceEntity extends BaseEntity {

    private String invoiceNumber;
    private String currency;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private UserEntity user;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private LegalEntity supplier;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private LegalEntity customer;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private BankAccountEntity supplierBankAccount;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<AssetEntity> assets;
    private String note;

}

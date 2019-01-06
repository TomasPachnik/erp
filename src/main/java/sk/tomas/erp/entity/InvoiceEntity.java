package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoice")
@Inheritance(strategy = InheritanceType.JOINED)
public class InvoiceEntity extends OwnerEntity {

    private String name;
    private String invoiceNumber;
    private String currency;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private UserEntity user;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private LegalEntity supplier;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private LegalEntity customer;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<AssetEntity> assets;
    private String note;

}

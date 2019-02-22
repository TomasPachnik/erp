package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    private UserEntity user;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    private LegalEntity supplier;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    private LegalEntity customer;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    private Date payDate;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<AssetEntity> assets;
    private BigDecimal total;
    private String note;

}

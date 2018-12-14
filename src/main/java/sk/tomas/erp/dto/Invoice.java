package sk.tomas.erp.dto;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoice")
public class Invoice extends BaseEntity {

    private String currency;
    private Issuer issuer;
    private String invoiceNumber;
    private LegalEntity supplier;
    private LegalEntity customer;
    private BankAccount supplierBankAccount;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    @ElementCollection
    private List<Asset> assets;

}

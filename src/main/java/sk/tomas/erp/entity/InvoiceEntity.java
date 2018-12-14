package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoice")
public class InvoiceEntity extends BaseEntity {

    private String currency;
    private IssuerEntity issuer;
    private String invoiceNumber;
    private LegalEntity supplier;
    private LegalEntity customer;
    private BankAccountEntity supplierBankAccount;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    @ElementCollection
    private List<AssetEntity> assets;

}

package sk.tomas.erp.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
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
    //@OneToMany(mappedBy = "asset")
    //private List<Asset> assets;
/*
    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;
        for (Asset asset : assets) {
            total = total.add(asset.total());
        }
        return total;
    }
*/
}

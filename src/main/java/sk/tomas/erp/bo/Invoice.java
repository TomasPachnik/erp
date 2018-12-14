package sk.tomas.erp.bo;

import lombok.Data;
import sk.tomas.erp.dto.BankAccount;
import sk.tomas.erp.dto.Issuer;
import sk.tomas.erp.dto.LegalEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Invoice {

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
    private List<Asset> assets;

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;
        for (Asset asset : assets) {
            total = total.add(asset.total());
        }
        return total;
    }


}

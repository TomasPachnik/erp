package sk.tomas.erp.bo;

import lombok.Data;
import sk.tomas.erp.entity.BankAccountEntity;
import sk.tomas.erp.entity.IssuerEntity;
import sk.tomas.erp.entity.LegalEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class Invoice {

    private UUID uuid;
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
    private List<Asset> assets;

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;
        for (Asset asset : assets) {
            total = total.add(asset.total());
        }
        return total;
    }


}

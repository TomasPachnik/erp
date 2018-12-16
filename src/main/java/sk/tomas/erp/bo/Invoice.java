package sk.tomas.erp.bo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class Invoice {

    private UUID uuid;
    private String invoiceNumber;
    private String currency;
    private Issue issuer;
    private Legal supplier;
    private Legal customer;
    private BankAccount supplierBankAccount;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    private List<Asset> assets;
    private String note;

    @JsonGetter("total")
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Asset asset : assets) {
            total = total.add(asset.getTotal());
        }
        return total;
    }


}

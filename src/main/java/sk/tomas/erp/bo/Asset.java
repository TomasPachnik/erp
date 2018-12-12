package sk.tomas.erp.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Asset {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;


    public BigDecimal total() {
        return count.multiply(unitPrice);
    }
}

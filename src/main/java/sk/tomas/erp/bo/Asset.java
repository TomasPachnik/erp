package sk.tomas.erp.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Asset implements Serializable {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;


    public BigDecimal total() {
        return count.multiply(unitPrice);
    }
}

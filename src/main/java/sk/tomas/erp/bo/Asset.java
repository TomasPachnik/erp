package sk.tomas.erp.bo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Asset {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;

    @JsonGetter("total")
    public BigDecimal getTotal() {
        return count.multiply(unitPrice);
    }

}

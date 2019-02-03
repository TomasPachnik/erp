package sk.tomas.erp.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Asset extends Base {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;

}

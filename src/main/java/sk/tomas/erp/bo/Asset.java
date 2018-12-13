package sk.tomas.erp.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "asset")
public class Asset extends BaseEntity {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;

    public BigDecimal total() {
        return count.multiply(unitPrice);
    }
}

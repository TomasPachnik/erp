package sk.tomas.erp.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "asset")
public class Asset extends BaseEntity {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;

}

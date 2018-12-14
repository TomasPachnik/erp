package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "asset")
public class AssetEntity extends BaseEntity {

    private String name;
    private BigDecimal count;
    private String unit;
    private BigDecimal unitPrice;

}

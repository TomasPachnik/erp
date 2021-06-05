package sk.tomas.erp.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Last12Months {

    private Date from;
    private BigDecimal amount;
    private int invoiceCount;

}

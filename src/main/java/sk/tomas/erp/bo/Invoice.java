package sk.tomas.erp.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Invoice extends Base {

    private String name;
    private String invoiceNumber;
    private String currency;
    private User user;
    private Supplier supplier;
    private Customer customer;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    private List<Asset> assets;
    private String note;
    private BigDecimal total;

}

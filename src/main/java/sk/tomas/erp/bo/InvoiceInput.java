package sk.tomas.erp.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceInput extends Base {

    private String name;
    private String invoiceNumber;
    private String currency;
    private UUID supplier;
    private UUID customer;
    private String supplierVariableSymbol;
    private Date dateOfIssue;
    private Date deliveryDate;
    private Date dueDate;
    private Date payDate;
    private List<Asset> assets;
    private String note;

}

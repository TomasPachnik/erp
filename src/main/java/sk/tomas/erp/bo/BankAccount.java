package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class BankAccount extends Base{

    private String bankName;
    private String iban;

}

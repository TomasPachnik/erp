package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class BankAccount {

    private String bankName;
    private String iban;
    private String swift;

}

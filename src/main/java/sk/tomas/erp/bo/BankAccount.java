package sk.tomas.erp.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BankAccount implements Serializable {

    private String bankName;
    private String iban;
    private String swift;

}

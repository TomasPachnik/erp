package sk.tomas.erp.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccount extends BaseEntity {

    private String bankName;
    private String iban;
    private String swift;

}

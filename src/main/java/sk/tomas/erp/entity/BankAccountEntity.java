package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccountEntity extends BaseEntity {

    private String bankName;
    private String iban;
    private String swift;

}

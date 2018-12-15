package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_password")
public class PasswordEntity extends BaseEntity {

    private String hash;
    private String salt;

}

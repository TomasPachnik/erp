package sk.tomas.erp.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "issuer")
public class Issuer extends BaseEntity {

    private String name;
    private String phone;
    private String email;

}

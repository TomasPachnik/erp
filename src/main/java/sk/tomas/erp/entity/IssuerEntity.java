package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "issuer")
public class IssuerEntity extends BaseEntity {

    private String name;
    private String phone;
    private String email;

}

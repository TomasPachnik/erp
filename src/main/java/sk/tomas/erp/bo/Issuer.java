package sk.tomas.erp.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "issuer")
public class Issuer extends BaseEntity {

    private String name;
    private String phone;
    private String email;

}

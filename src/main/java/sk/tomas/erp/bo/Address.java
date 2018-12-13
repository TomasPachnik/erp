package sk.tomas.erp.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String town;
    private String country;

}

package sk.tomas.erp.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String town;
    private String country;


}

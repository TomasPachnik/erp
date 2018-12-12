package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class Address {

    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String town;
    private String country;


}

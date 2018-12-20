package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class Supplier extends Base {

    private String name;
    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;

}

package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class Legal {

    private String name;
    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;

}

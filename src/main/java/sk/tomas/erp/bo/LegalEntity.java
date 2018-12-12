package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class LegalEntity {

    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;

}

package sk.tomas.erp.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LegalEntity implements Serializable {

    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;

}

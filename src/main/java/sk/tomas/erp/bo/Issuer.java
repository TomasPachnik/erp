package sk.tomas.erp.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Issuer implements Serializable {

    private String name;
    private String phone;
    private String email;

}

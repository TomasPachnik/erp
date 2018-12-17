package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class User extends Base {

    private String name;
    private String login;
    private String phone;
    private String email;

}

package sk.tomas.erp.bo;

import lombok.Data;


@Data
public class User extends Base {

    private String login;
    private String name;
    private String email;
    private String phone;
    private boolean enabled;

}

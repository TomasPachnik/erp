package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class ChangePassword {

    private String oldPassword;
    private String newPassword;

    @Override
    public String toString() {
        return "ChangePassword{}";
    }
}

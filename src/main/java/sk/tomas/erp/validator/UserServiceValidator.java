package sk.tomas.erp.validator;

import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.ChangeUser;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.exception.InputValidationException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class UserServiceValidator {
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static void validateInput(User user) {
        BaseValidator.validateNull(user, "User");
        validateLogin(user.getUsername());
        validate(user.getName(), user.getEmail());
    }

    public static void validateInput(ChangeUser user) {
        BaseValidator.validateNull(user, "User");
        validate(user.getName(), user.getEmail());
        BaseValidator.validateMaxLength(user.getPhone(), 30, "phone number");
    }

    public static void validateLogin(String login) {
        BaseValidator.validateNullOrEmpty(login, "Login");
        BaseValidator.validateMaxLength(login, 30, "login");
    }

    public static void validatePassword(ChangePassword password) {
        BaseValidator.validateNull(password, "Password");
        BaseValidator.validateNullOrEmpty(password.getOldPassword(), "Old password");
        BaseValidator.validateNullOrEmpty(password.getNewPassword(), "New password");
        BaseValidator.validateMaxLength(password.getNewPassword(), 30, "password");
        if (!password.getNewPassword().matches(PASSWORD_REGEX)) {
            throw new InputValidationException("New password must have a digit, lower case, upper case, and at last 8 chars.");
        }
    }

    private static void validate(String name, String email) {
        BaseValidator.validateNullOrEmpty(name, "Name");
        BaseValidator.validateMaxLength(name, 30, "name");
        BaseValidator.validateNullOrEmpty(email, "Email");
        BaseValidator.validateMaxLength(email, 60, "email");
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            throw new InputValidationException("Email is not valid.");
        }
    }

}

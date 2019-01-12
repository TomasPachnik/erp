package sk.tomas.erp.validator;

import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.ChangeUser;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.exception.InputValidationException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.UUID;

public class UserServiceValidator {
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static void validateInput(User user) {
        if (user == null) {
            throw new InputValidationException("User is mandatory.");
        }
        validateLogin(user.getLogin());
        validate(user.getName(), user.getEmail());
    }

    public static void validateInput(ChangeUser user) {
        if (user == null) {
            throw new InputValidationException("User is mandatory.");
        }
        validate(user.getName(), user.getEmail());
    }

    public static void validateLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new InputValidationException("Login is mandatory.");
        }
        if (login.length() > 30) {
            throw new InputValidationException("Maximal length of login is 30 characters.");
        }
    }

    public static void validateUuid(UUID uuid) {
        if (uuid == null) {
            throw new InputValidationException("UUID is mandatory.");
        }
    }

    public static void validatePassword(ChangePassword password) {
        if (password == null) {
            throw new InputValidationException("Password is mandatory.");
        }
        if (password.getOldPassword() == null || password.getOldPassword().isEmpty()) {
            throw new InputValidationException("Old password is mandatory.");
        }
        if (password.getNewPassword() == null || password.getNewPassword().isEmpty()) {
            throw new InputValidationException("New password is mandatory.");
        }
        if (password.getNewPassword().length() > 30) {
            throw new InputValidationException("Maximal length of password is 30 characters.");
        }
        if (!password.getNewPassword().matches(PASSWORD_REGEX)) {
            throw new InputValidationException("New password must have a digit, lower case, upper case, and at last 8 chars.");
        }
    }

    private static void validate(String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new InputValidationException("Name is mandatory.");
        }
        if (name.length() > 50) {
            throw new InputValidationException("Maximal length of name is 50 characters.");
        }
        if (email == null || email.isEmpty()) {
            throw new InputValidationException("Email is mandatory.");
        }
        if (email.length() > 100) {
            throw new InputValidationException("Maximal length of email is 100 characters.");
        }
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            throw new InputValidationException("Email is not valid.");
        }
    }

}

package sk.tomas.erp.validator;

import sk.tomas.erp.bo.StringInput;
import sk.tomas.erp.exception.InputValidationException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class AuditValidator {

    public static void validateStringInput(StringInput input) {
        BaseValidator.validateNull(input, "StringInput");
        BaseValidator.validateMaxLength(input.getValue(), 60, "email");
        try {
            InternetAddress emailAddress = new InternetAddress(input.getValue());
            emailAddress.validate();
        } catch (AddressException e) {
            throw new InputValidationException("Email is not valid.");
        }
    }
}

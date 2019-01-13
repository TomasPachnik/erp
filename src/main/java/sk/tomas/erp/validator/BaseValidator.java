package sk.tomas.erp.validator;

import sk.tomas.erp.exception.InputValidationException;

import java.util.UUID;

public class BaseValidator {

    public static void validateUuid(UUID uuid) {
        if (uuid == null) {
            throw new InputValidationException("UUID is mandatory.");
        }
    }

    public static void validateUuid(UUID uuid, String name) {
        if (uuid == null) {
            throw new InputValidationException("UUID of " + name + " is mandatory.");
        }
    }

    static void validateNull(Object input, String name) {
        if (input == null) {
            throw new InputValidationException(name + " is mandatory.");
        }
    }

    static void validateNullOrEmpty(String input, String name) {
        if (input == null || input.isEmpty()) {
            throw new InputValidationException(name + " is mandatory.");
        }
    }

    static void validateMaxLength(String input, int maxLength, String name) {
        if (input.length() > maxLength) {
            throw new InputValidationException("Maximal length of " + name + " is " + maxLength + " characters.");
        }
    }


}

package sk.tomas.erp.exception;

public class ValidationException extends ApplicationRuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}

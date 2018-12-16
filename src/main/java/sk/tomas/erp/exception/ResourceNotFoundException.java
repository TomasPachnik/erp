package sk.tomas.erp.exception;

public class ResourceNotFoundException extends ApplicationRuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}

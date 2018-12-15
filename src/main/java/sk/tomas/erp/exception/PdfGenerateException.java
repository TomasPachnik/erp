package sk.tomas.erp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PdfGenerateException extends ApplicationRuntimeException {

    public PdfGenerateException(String message) {
        super(message);
    }

    public PdfGenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}

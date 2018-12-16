package sk.tomas.erp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.bo.ServerMessage;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@ControllerAdvice
public class ExceptionHandlerController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
    private static final String PATH = "/error";

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ServerMessage businessException(AccessDeniedException e) {
        return new ServerMessage("AccessDeniedException", "Forbidden");
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ServerMessage handleAllException(Exception e) {
        UUID uuid = UUID.randomUUID();
        logger.error(uuid.toString(), e);
        return new ServerMessage("InternalServerError", "internal server error, error code: " + uuid);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}

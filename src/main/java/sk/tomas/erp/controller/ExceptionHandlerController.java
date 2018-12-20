package sk.tomas.erp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.erp.bo.ServerMessage;
import sk.tomas.erp.exception.SqlException;
import sk.tomas.erp.exception.TokenException;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController implements ErrorController {

    private static final String PATH = "/error";

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ServerMessage businessException(AccessDeniedException e) {
        return new ServerMessage("AccessDeniedException", "Forbidden");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SqlException.class)
    public ServerMessage sqlException(SqlException e) {
        return new ServerMessage("SqlException", "constraint exception");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenException.class)
    public ServerMessage tokenException(TokenException e) {
        return new ServerMessage("tokenException", "Expired or invalid JWT token");
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ServerMessage handleAllException(Exception e) {
        UUID uuid = UUID.randomUUID();
        log.error(uuid.toString(), e);
        return new ServerMessage("InternalServerError", "internal server error, error code: " + uuid);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}

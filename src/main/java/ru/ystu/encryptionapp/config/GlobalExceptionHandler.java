package ru.ystu.encryptionapp.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.ystu.encryptionapp.dto.ApiErrorDTO;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(
                        new ApiErrorDTO.Builder()
                                .withStatus(HttpStatus.BAD_REQUEST)
                                .withResponse(e.getMessage())
                                .withCause(e.getCause())
                                .build()
                );
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidFormatException(InvalidFormatException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ApiErrorDTO.Builder()
                                .withStatus(HttpStatus.BAD_REQUEST)
                                .withResponse(e.getMessage())
                                .withCause(e.getCause())
                                .build()
                );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String exceptionNotFound(final NoHandlerFoundException exception) {
        log.error("Exception during execution of application", exception);

        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionInternalServerError(final Exception exception) {
        log.error("Exception during execution of application", exception);

        return "error/error";
    }
}

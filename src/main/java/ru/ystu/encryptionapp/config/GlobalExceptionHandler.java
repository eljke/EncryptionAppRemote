package ru.ystu.encryptionapp.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.ystu.encryptionapp.dto.ApiErrorDTO;

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
}

package ru.ystu.encryptionapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ystu.encryptionapp.dto.ApiBaseDTO;
import ru.ystu.encryptionapp.dto.ApiErrorDTO;
import ru.ystu.encryptionapp.dto.ApiResponseDTO;
import ru.ystu.encryptionapp.entity.UserEntity;
import ru.ystu.encryptionapp.service.UserService;

@RestController
public class RegistrationController {
    private final UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/api/register")
    public ResponseEntity<ApiBaseDTO> registerUser(@RequestBody UserEntity user) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.CREATED)
                                    .withResponse(service.createUser(user))
                                    .build()
                    );
        } catch (Exception e) {
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
    }
}

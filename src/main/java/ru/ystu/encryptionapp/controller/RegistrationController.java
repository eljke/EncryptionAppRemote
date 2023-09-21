package ru.ystu.encryptionapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.UserEntity;
import ru.ystu.encryptionapp.service.UserService;

@RestController
public class RegistrationController {
    private final UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(user));
    }
}

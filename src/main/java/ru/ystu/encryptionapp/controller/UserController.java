package ru.ystu.encryptionapp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.UserEntity;
import ru.ystu.encryptionapp.service.UserService;

import java.util.UUID;

@Hidden
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.getUserById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false, defaultValue = "joinDate") String... sortFields
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(sortOrder.toUpperCase()), sortFields);

        return ResponseEntity.ok(service.getAllUsers(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(
            @PathVariable String id,
            @RequestBody UserEntity user
    ) {
        return ResponseEntity.ok(service.updateUserById(UUID.fromString(id), user));
    }

    @PutMapping("/@{username}")
    public ResponseEntity<UserDTO> updateUserByUsername(
            @PathVariable String username,
            @RequestBody UserEntity user
    ) {
        return ResponseEntity.ok(service.updateUserByUsername(username, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(service.deleteUserById(UUID.fromString(id)));
    }

    @DeleteMapping("/@{username}")
    public ResponseEntity<?> deleteUserByUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(service.deleteUserByUsername(username));
    }
}

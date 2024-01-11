package ru.ystu.encryptionapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ystu.encryptionapp.dto.ApiBaseDTO;
import ru.ystu.encryptionapp.dto.ApiErrorDTO;
import ru.ystu.encryptionapp.dto.ApiResponseDTO;
import ru.ystu.encryptionapp.service.DatabaseService;

@Slf4j
@RequestMapping("/admin")
@RestController
public class AdminController {
    private final DatabaseService databaseService;

    public AdminController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @DeleteMapping("/truncate")
    public ResponseEntity<ApiBaseDTO> truncateTables() {
        try {
            databaseService.clearAllTables();

            return ResponseEntity
                    .ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse("All tables are cleared")
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }
}

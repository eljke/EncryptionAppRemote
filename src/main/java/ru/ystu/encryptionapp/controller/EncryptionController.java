package ru.ystu.encryptionapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import ru.ystu.encryptionapp.dto.*;
import ru.ystu.encryptionapp.entity.EncodeRequestAndEncryptedValue;
import ru.ystu.encryptionapp.entity.EncryptionAlgorithm;
import ru.ystu.encryptionapp.exception.EncryptionAlgorithmNotFoundException;
import ru.ystu.encryptionapp.mapper.UserMapper;
import ru.ystu.encryptionapp.service.EncodeRequestAndEncryptedValueService;
import ru.ystu.encryptionapp.service.EncryptionService;
import ru.ystu.encryptionapp.service.UserService;

import java.util.UUID;

import static ru.ystu.encryptionapp.controller.AccountController.getCombinedUsername;

@RestController
@RequestMapping("/api/encryption")
public class EncryptionController {
    private final EncryptionService encryptionService;
    private final EncodeRequestAndEncryptedValueService encodeRequestAndEncryptedValueService;
    private final UserService userService;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public EncryptionController(EncryptionService encryptionService, EncodeRequestAndEncryptedValueService encodeRequestAndEncryptedValueService, UserService userService) {
        this.encryptionService = encryptionService;
        this.encodeRequestAndEncryptedValueService = encodeRequestAndEncryptedValueService;
        this.userService = userService;
    }

    @PostMapping("/encode")
    public ResponseEntity<ApiBaseDTO> encode(@RequestBody final EncodeRequestDTO encodeRequestDTO) {
        LOG.info(encodeRequestDTO.toString());
        try {
            return ResponseEntity
                    .ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.encode(encodeRequestDTO))
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

    @PostMapping("/decode")
    public ResponseEntity<ApiBaseDTO> decode(@RequestBody final DecodeRequestDTO decodeRequestDTO) {
        LOG.info(decodeRequestDTO.toString());
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.decode(decodeRequestDTO))
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @PostMapping("/custom/save")
    public ResponseEntity<ApiBaseDTO> save(@RequestBody final EncryptionAlgorithm encryptionAlgorithm) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.save(encryptionAlgorithm))
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @GetMapping("/custom/all")
    public ResponseEntity<ApiBaseDTO> getAll() {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.getAll())
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @GetMapping("/custom/@{name}")
    public ResponseEntity<ApiBaseDTO> getByName(@PathVariable final String name) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.getByName(name))
                                    .build()
                    );
        } catch (EncryptionAlgorithmNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @GetMapping("/custom/{id}")
    public ResponseEntity<ApiBaseDTO> getById(@PathVariable final String id) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.getById(id))
                                    .build()
                    );
        } catch (EncryptionAlgorithmNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @PutMapping("/custom/@{name}")
    public ResponseEntity<ApiBaseDTO> updateByName(@PathVariable final String name, @RequestBody final EncryptionAlgorithm algorithm) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.updateByName(name, algorithm))
                                    .build()
                    );
        } catch (EncryptionAlgorithmNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @PutMapping("/custom/{id}")
    public ResponseEntity<ApiBaseDTO> updateById(@PathVariable final String id, @RequestBody final EncryptionAlgorithm algorithm) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.updateById(id, algorithm))
                                    .build()
                    );
        } catch (EncryptionAlgorithmNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @DeleteMapping("/custom/@{name}")
    public ResponseEntity<ApiBaseDTO> deleteByName(@PathVariable final String name) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.deleteByName(name))
                                    .build()
                    );
        } catch (EncryptionAlgorithmNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @Operation(hidden = true)
    @DeleteMapping("/custom/{id}")
    public ResponseEntity<ApiBaseDTO> deleteById(@PathVariable final String id) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encryptionService.deleteById(id))
                                    .build()
                    );
        } catch (EncryptionAlgorithmNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @PostMapping("/result/save")
    public ResponseEntity<ApiBaseDTO> saveResult(@RequestBody final EncodeRequestAndEncryptedValue encodeRequestAndEncryptedValue, final Authentication auth) {
        try {
            String username;
            // For Google
            if (auth.getPrincipal() instanceof DefaultOidcUser oidcUser) {
                String email = oidcUser.getEmail();
                UserDTO userFromDB = userService.getUserByUsername(email);
                username = userFromDB.getUsername();
                // For Discord, GitHub, VK
            } else if (auth.getPrincipal() instanceof DefaultOAuth2User) {
                String combinedUsername = getCombinedUsername(auth);
                UserDTO userFromDB = userService.getUserByUsername(combinedUsername);
                username = userFromDB.getUsername();
            } else {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserDTO userFromDB = userService.getUserByUsername(userDetails.getUsername());
                username = userFromDB.getUsername();
            }
            LOG.info(username);
            encodeRequestAndEncryptedValue.setUser(UserMapper.INSTANCE.userDTOToUser(userService.getUserByUsername(username)));
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encodeRequestAndEncryptedValueService.save(encodeRequestAndEncryptedValue))
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @GetMapping("/result/all")
    public ResponseEntity<ApiBaseDTO> getAllResults(final @RequestParam(required = false) String username) {
        try {
            UserDTO userFromDB = userService.getUserByUsername(username);

            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encodeRequestAndEncryptedValueService.getAllByUser(UserMapper.INSTANCE.userDTOToUser(userFromDB)))
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiErrorDTO.Builder()
                                    .withStatus(HttpStatus.BAD_REQUEST)
                                    .withResponse(e.getMessage())
                                    .withCause(e.getCause())
                                    .build()
                    );
        }
    }

    @DeleteMapping("/result/{id}")
    public ResponseEntity<ApiBaseDTO> deleteResultById(final @PathVariable UUID id) {
        try {
            return ResponseEntity.ok()
                    .body(
                            new ApiResponseDTO.Builder()
                                    .withStatus(HttpStatus.OK)
                                    .withResponse(encodeRequestAndEncryptedValueService.deleteById(id))
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
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

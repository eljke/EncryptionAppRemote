package ru.ystu.encryptionapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.ystu.encryptionapp.dto.ApiBaseDTO;
import ru.ystu.encryptionapp.dto.EncodeRequestDTO;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.entity.EncryptionAlgorithm;
import ru.ystu.encryptionapp.enumeration.AlgorithmType;
import ru.ystu.encryptionapp.service.UserService;

import java.util.Collections;
import java.util.Objects;

import static ru.ystu.encryptionapp.controller.AccountController.getCombinedUsername;

@Slf4j
@Controller
@RequestMapping("/encryption")
public class EncryptionControllerUI {
    @Value("${app.hostingAddress}")
    private String hostingAddress;
    private final RestTemplate restTemplate;
    private final UserService userService;

    public EncryptionControllerUI(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @GetMapping("/encode")
    public String encode(final Model model) {
        model.addAttribute("request", EncodeRequestDTO.class);
        model.addAttribute("encryptionAlgorithm", EncryptionAlgorithm.class);
        model.addAttribute("algorithmTypes", AlgorithmType.values());

        return "encryption/encode";
    }

    @GetMapping("/decode")
    public String decode(final Model model) {
        model.addAttribute("request", EncodeRequestDTO.class);
        model.addAttribute("encryptionAlgorithm", EncryptionAlgorithm.class);
        model.addAttribute("algorithmTypes", AlgorithmType.values());

        return "encryption/decode";
    }

    @GetMapping("/custom/save")
    public String save(final Model model) {
        String apiUrl = hostingAddress + "/api/encryption/custom/save";
        ResponseEntity<ApiBaseDTO> response = restTemplate.getForEntity(apiUrl, ApiBaseDTO.class);
        model.addAttribute("data", response.getBody());

        return "encryption/create-algorithm";
    }

    @GetMapping("/result/all")
    public String getAllResults(final Model model, final Authentication auth) {
        String username;
        if (auth != null) {
            // For VK
            if (auth.getPrincipal() instanceof DefaultOAuth2User) {
                String combinedUsername = getCombinedUsername(auth);
                UserDTO userFromDB = userService.getUserByUsername(combinedUsername);
                username = userFromDB.getUsername();
            } else {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                UserDTO userFromDB = userService.getUserByUsername(userDetails.getUsername());
                username = userFromDB.getUsername();
            }
            String apiUrl = hostingAddress + "/api/encryption/result/all?username=" + username;
            ResponseEntity<ApiBaseDTO> response = restTemplate.getForEntity(apiUrl, ApiBaseDTO.class);
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("results", Objects.requireNonNull(response.getBody()).getResponse());
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("results", Collections.emptyList());
        }

        return "encryption/saved-results";
    }
}

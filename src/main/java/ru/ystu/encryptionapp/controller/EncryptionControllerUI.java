package ru.ystu.encryptionapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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

import java.util.Objects;

import static ru.ystu.encryptionapp.controller.AccountController.getCombinedUsername;

@Controller
@RequestMapping("/encryption")
public class EncryptionControllerUI {
    @Value("${app.host}")
    private String host;

    @Value("${app.port}")
    private int port;
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
        String apiUrl = "http://" + host + ":" + port + "/api/encryption/custom/save";
        ResponseEntity<ApiBaseDTO> response = restTemplate.getForEntity(apiUrl, ApiBaseDTO.class);
        model.addAttribute("data", response.getBody());

        return "encryption/create-algorithm";
    }

    @GetMapping("/result/all")
    public String getAllResults(final Model model, final Authentication auth) {
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
        String apiUrl = "http://" + host + ":" + port + "/api/encryption/result/all?username=" + username;
        ResponseEntity<ApiBaseDTO> response = restTemplate.getForEntity(apiUrl, ApiBaseDTO.class);
        model.addAttribute("results", Objects.requireNonNull(response.getBody()).getResponse());

        return "encryption/saved-results";
    }
}

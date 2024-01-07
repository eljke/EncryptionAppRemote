package ru.ystu.encryptionapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ystu.encryptionapp.dto.UserDTO;
import ru.ystu.encryptionapp.service.UserService;

import java.util.Objects;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService service;
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    public AccountController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public String isAuthenticated(Authentication auth, Model model) {
        if (auth != null) {
            try {
                 // For VK
                 if (auth.getPrincipal() instanceof DefaultOAuth2User) {
                    String combinedUsername = getCombinedUsername(auth);
                    UserDTO userFromDB = service.getUserByUsername(combinedUsername);
                    model.addAttribute("user", userFromDB);
                    LOG.info("User logged in with OAuth: {}", userFromDB);
                } else {
                    UserDetails userDetails = (UserDetails) auth.getPrincipal();
                    UserDTO userFromDB = service.getUserByUsername(userDetails.getUsername());
                    model.addAttribute("user", userFromDB);
                    LOG.info("User logged in with local provider: {}", userFromDB);
                }
            } catch (Exception e) {
                LOG.error("Exception: ", e);
            }
        }

        return "account";
    }

    public static String getCombinedUsername(Authentication auth) {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) auth.getPrincipal();
        if (oAuth2User.getAttribute("first_name") != null
                && oAuth2User.getAttribute("last_name") != null
                && oAuth2User.getAttribute("id") != null) {
            return
                    "VK.ID " +
                    Objects.requireNonNull(oAuth2User.getAttribute("id")) +
                    " " +
                    Objects.requireNonNull(oAuth2User.getAttribute("first_name")) +
                    " " +
                    Objects.requireNonNull(oAuth2User.getAttribute("last_name"));
        } else if (oAuth2User.getAttribute("login") != null
                && oAuth2User.getAttribute("id") != null) {
            return
                    "GitHub.ID " +
                    Objects.requireNonNull(oAuth2User.getAttribute("id")) +
                    " " +
                    Objects.requireNonNull(oAuth2User.getAttribute("login"));
        } else if (oAuth2User.getAttribute("username") != null
                && oAuth2User.getAttribute("email") != null) {
            return
                    "Discord.ID " +
                    Objects.requireNonNull(oAuth2User.getAttribute("id")) +
                    " " +
                    Objects.requireNonNull(oAuth2User.getAttribute("username"));
    }

        return null;
    }
}

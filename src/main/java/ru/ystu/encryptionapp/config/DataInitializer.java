package ru.ystu.encryptionapp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.ystu.encryptionapp.entity.UserEntity;
import ru.ystu.encryptionapp.enumeration.Role;
import ru.ystu.encryptionapp.exception.UserNotFoundException;
import ru.ystu.encryptionapp.service.UserService;

import java.util.Collections;

@Configuration
public class DataInitializer {
    private final UserService service;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DataInitializer(UserService service, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.service = service;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void initData() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "system", null,
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        try {
            createUser("user", "user", Role.USER);
            createUser("admin", "admin", Role.ADMIN);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void createUser(String username, String password, Role role) {
        try {
            service.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setName(username);
            userEntity.setEmail(username + "@" + username + "." + username);
            userEntity.setPassword(bCryptPasswordEncoder.encode(password));
            userEntity.setRole(role);

            service.createUser(userEntity);
        }
    }
}


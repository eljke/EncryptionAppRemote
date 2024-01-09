package ru.ystu.encryptionapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.ystu.encryptionapp.utils.BCryptPasswordEncoderWrapper;

@Slf4j
@Configuration
public class InMemoryUsers {
    @Value("${ADMIN_PASSWORD:admin}")
    private String adminPassword;
    private final BCryptPasswordEncoderWrapper bCryptPasswordEncoderWrapper;

    public InMemoryUsers(BCryptPasswordEncoderWrapper bCryptPasswordEncoderWrapper) {
        this.bCryptPasswordEncoderWrapper = bCryptPasswordEncoderWrapper;
    }

    @Bean
    public UserDetailsService get() {
        UserDetails user = User.builder()
                .username("user")
                .password(bCryptPasswordEncoderWrapper.getEncoder().encode("user"))
                .authorities("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoderWrapper.getEncoder().encode(adminPassword))
                .authorities("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}

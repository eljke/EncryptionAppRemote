package ru.ystu.encryptionapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import ru.ystu.encryptionapp.utils.BCryptPasswordEncoderWrapper;

@Component
public class InMemoryUsers {
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
                .password(bCryptPasswordEncoderWrapper.getEncoder().encode("admin"))
                .authorities("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}

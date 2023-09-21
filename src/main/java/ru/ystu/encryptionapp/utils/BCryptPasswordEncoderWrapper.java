package ru.ystu.encryptionapp.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderWrapper {
    @Bean
    public org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder getEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}

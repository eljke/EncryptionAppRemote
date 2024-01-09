package ru.ystu.encryptionapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

@Slf4j
@Configuration
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    public LogoutSuccessHandler() {
        super();
        setUseReferer(true);
    }

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String redirectUrl = (String) request.getSession().getAttribute("prevPage");

        response.sendRedirect(redirectUrl != null ? redirectUrl : "/");
    }
}

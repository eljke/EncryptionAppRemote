package ru.ystu.encryptionapp.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Configuration
public class RequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        HttpSession session = request.getSession();String requestURI = request.getRequestURI();

        if (!requestURI.contains("/api/" )&& !requestURI.endsWith(".png") && !requestURI.endsWith(".jpg") && !requestURI.endsWith(".webp")
                && !requestURI.endsWith(".jpeg" )&& !requestURI.endsWith(".mp4") && !requestURI.endsWith(".css") && !requestURI.endsWith(".js")) {
            session.setAttribute("prevPage", request.getRequestURL().toString());
        }

        filterChain.doFilter(request, response);
    }
}


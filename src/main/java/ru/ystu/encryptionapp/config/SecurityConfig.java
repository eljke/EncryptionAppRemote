package ru.ystu.encryptionapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.web.client.RestTemplate;
import ru.ystu.encryptionapp.enumeration.Provider;
import ru.ystu.encryptionapp.service.UserService;
import ru.ystu.encryptionapp.utils.BCryptPasswordEncoderWrapper;
import ru.ystu.encryptionapp.utils.auth.service.VKOAuth2UserService;

import java.util.*;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final BCryptPasswordEncoderWrapper bCryptPasswordEncoderWrapper;
    private final InMemoryUsers inMemoryUsers;
    private Provider provider;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public SecurityConfig(UserService userService, BCryptPasswordEncoderWrapper bCryptPasswordEncoderWrapper, InMemoryUsers inMemoryUsers, VKOAuth2UserService vkOAuth2UserService) {
        this.userService = userService;
        this.bCryptPasswordEncoderWrapper = bCryptPasswordEncoderWrapper;
        this.inMemoryUsers = inMemoryUsers;
        this.vkOAuth2UserService = vkOAuth2UserService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService).passwordEncoder(bCryptPasswordEncoderWrapper.getEncoder());
        auth
                .userDetailsService(inMemoryUsers.get())
                .passwordEncoder(bCryptPasswordEncoderWrapper.getEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requestCache((cache) -> cache
                        .requestCache(new HttpSessionRequestCache()).disable()
                )
                .passwordManagement((management) -> management
                        .changePasswordPage("/change-password"))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl("/account")
                )
                .logout(withDefaults())
                .cors(withDefaults())
                .oauth2Login(withDefaults())
                .oauth2Login(oauth2 -> oauth2
                        .tokenEndpoint(token -> token
                                .accessTokenResponseClient(accessTokenResponseVKClient())
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(vkOAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            // TODO: Имплементировать остальных поставщиков OAuth2 помимо Google, Github, Discord и VK
                            try {
                                if (authentication.getPrincipal() instanceof DefaultOidcUser defaultOidcUser) {
                                    String email = defaultOidcUser.getEmail();
                                    String name = defaultOidcUser.getName();
                                    userService.processGoogleOAuth2PostLogin(email, name);
                                    response.sendRedirect("/account");
                                } else if (authentication.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {
                                    String combinedUsername = getCombinedUsername(oAuth2User);
                                    if (provider == Provider.VK) {
                                        String name = oAuth2User.getAttribute("first_name") + " " + oAuth2User.getAttribute("last_name");
                                        userService.processVKOAuth2PostLogin(combinedUsername, name);
                                    } else if (provider == Provider.GITHUB) {
                                        String name = oAuth2User.getAttribute("name");
                                        userService.processGithubOAuth2PostLogin(combinedUsername, name);
                                    } else if (provider == Provider.DISCORD) {
                                        String name = oAuth2User.getAttribute("global_name");
                                        userService.processDiscordOAuth2PostLogin(combinedUsername, name, oAuth2User.getAttribute("email"));
                                    }
                                    LOG.info(oAuth2User.getAttributes().toString());
                                    response.sendRedirect("/account");
                                } else {
                                    LOG.error("Unknown provider");
                                }
                            } catch (Exception e) {
                                LOG.error("Exception: ", e);
                            }
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("", "/", "/home", "/encode",
                                "/register", "/login", "/logout", "/change-password", "/oauth/**",
                                "/static/**", "/images/**", "/styles/**", "/scripts/**", "/docs/**", "/favicon.*",
                                "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/user/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/user/list", "/admin/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseVKClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();

        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(tokenResponseParameters -> {
            String accessToken = (String) tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);
            OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;
            Map<String, Object> additionalParameters = new HashMap<>(tokenResponseParameters);

            return OAuth2AccessTokenResponse.withToken(accessToken)
                    .tokenType(accessTokenType)
                    .additionalParameters(additionalParameters)
                    .build();
        });

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));

        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        accessTokenResponseClient.setRestOperations(restTemplate);

        return accessTokenResponseClient;
    }

    // OAuth2 User Services
    private final VKOAuth2UserService vkOAuth2UserService;

    private String getCombinedUsername(DefaultOAuth2User oAuth2User) {
        // For VK
        if (oAuth2User.getAttribute("first_name") != null && oAuth2User.getAttribute("last_name") != null) {
            provider = Provider.VK;
            return
                    "VK.ID " +
                            Objects.requireNonNull(oAuth2User.getAttribute("id")) +
                            " " +
                            Objects.requireNonNull(oAuth2User.getAttribute("first_name")) +
                            " " +
                            Objects.requireNonNull(oAuth2User.getAttribute("last_name"));
        // For GitHub
        } else if (oAuth2User.getAttribute("login") != null) {
            provider = Provider.GITHUB;
            return
                    "GitHub.ID " +
                            Objects.requireNonNull(oAuth2User.getAttribute("id")) +
                            " " +
                            Objects.requireNonNull(oAuth2User.getAttribute("login"));
        // For Discord
        } else if (oAuth2User.getAttribute("username") != null && oAuth2User.getAttribute("email") != null) {
            provider = Provider.DISCORD;
            return
                    "Discord.ID " +
                            Objects.requireNonNull(oAuth2User.getAttribute("id")) +
                            " " +
                            Objects.requireNonNull(oAuth2User.getAttribute("username"));
        }

        return null;
    }
}
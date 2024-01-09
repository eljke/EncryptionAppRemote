package ru.ystu.encryptionapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.ystu.encryptionapp.enumeration.Provider;
import ru.ystu.encryptionapp.service.UserService;
import ru.ystu.encryptionapp.utils.BCryptPasswordEncoderWrapper;
import ru.ystu.encryptionapp.utils.auth.service.VKOAuth2UserService;

import java.util.*;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final BCryptPasswordEncoderWrapper bCryptPasswordEncoderWrapper;
    private final InMemoryUsers inMemoryUsers;
    private Provider provider;
    private static final String API_URL_PATTERN = "/**";

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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrfConfigurer ->
                csrfConfigurer.ignoringRequestMatchers(mvcMatcherBuilder.pattern(API_URL_PATTERN),
                        PathRequest.toH2Console()));

        http.headers(headersConfigurer ->
                headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http
                .requestCache((cache) -> cache
                        .requestCache(new HttpSessionRequestCache()).disable()
                )
                .passwordManagement((management) -> management
                        .changePasswordPage("/change-password"))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl("/account")
                        .successHandler(new LoginSuccessHandler())
                )
                .logout(withDefaults())
                .logout(logout -> logout
                        .logoutSuccessHandler(new LogoutSuccessHandler())
                        .invalidateHttpSession(false)
                )
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
                            try {
                                if (authentication.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {
                                    String combinedUsername = getCombinedUsername(oAuth2User);
                                    if (provider == Provider.VK) {
                                        String name = oAuth2User.getAttribute("first_name") + " " + oAuth2User.getAttribute("last_name");
                                        userService.processVKOAuth2PostLogin(combinedUsername, name);
                                    }
                                    log.info(oAuth2User.getAttributes().toString());

                                    String redirectUrl = (String) request.getSession().getAttribute("prevPage");
                                    response.sendRedirect(redirectUrl != null ? redirectUrl : "/");
                                } else {
                                    log.error("Unknown provider");
                                }
                            } catch (Exception e) {
                                log.error("Exception: ", e);
                            }
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mvcMatcherBuilder.pattern(API_URL_PATTERN)).permitAll()
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
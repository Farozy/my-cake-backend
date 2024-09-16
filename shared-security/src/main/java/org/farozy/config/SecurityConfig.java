package org.farozy.config;

import lombok.RequiredArgsConstructor;
import org.farozy.enums.UserRoleType;
import org.farozy.exception.ErrorResponseBuilder;
import org.farozy.filter.CsrfTokenFilter;
import org.farozy.filter.JwtAuthenticationFilter;
import org.farozy.security.oauth2.CustomOAuth2AuthenticationFailureHandler;
import org.farozy.security.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import org.farozy.security.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;
    private final CorsConfigurationSource configurationSource;

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    private static final String[] PUBLIC_ROUTES = {
            "/public/**",
            "/api/auth/**",
            "/oauth2/**",
            "/auth/verify-token/confirm/**",
            "/login/**",
            "/register",
            "/api/otp/**",
            "/oauth2/callback/*",
            "/dashboard/**",
            "/home/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers(PUBLIC_ROUTES)
//                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(configurationSource))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .requestMatchers("/dashboard/**").hasAnyRole(UserRoleType.SUPER_ADMIN.getRole(), UserRoleType.ADMIN.getRole())
                        .requestMatchers("/home/**").hasRole(UserRoleType.USER.getRole())
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .permitAll()
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customOAuth2AuthenticationSuccessHandler)
                        .failureHandler(customOAuth2AuthenticationFailureHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "X-CSRF-TOKEN")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> ErrorResponseBuilder.handleError(
                                response, HttpStatus.UNAUTHORIZED, "Unauthorized", authException.getMessage()
                        ))
                        .accessDeniedHandler((request, response, accessDeniedException) -> ErrorResponseBuilder.handleError(
                                response, HttpStatus.FORBIDDEN, "Forbidden", accessDeniedException.getMessage()
                        ))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CsrfTokenFilter(csrfTokenRepository()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

package com.elyashevich.mmfask.config;

import com.elyashevich.mmfask.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomCorsConfiguration customCorsConfiguration;

    private final SecurityFilter securityFilter;
    private static final String ROLE_GUEST = "GUEST";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_MODERATOR = "MODERATOR";
    private static final String ROLE_ADMIN = "ADMIN";

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, "api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/images/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/posts/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/programming-languages/**").permitAll()
                                .requestMatchers(
                                        "/swagger-ui/*",
                                        "/swagger/*",
                                        "/v3/api-docs",
                                        "/v3/api-docs/swagger-config"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/users/**").hasRole(ROLE_GUEST)
                                .requestMatchers(HttpMethod.POST, "api/v1/users/**").hasRole(ROLE_GUEST)
                                .requestMatchers("api/v1/posts/**").hasRole(ROLE_USER)
                                .requestMatchers("api/v1/favorites/**").hasRole(ROLE_USER)
                                .requestMatchers(HttpMethod.POST, "api/v1/auth/reset-password/*").hasRole(ROLE_USER)
                                .requestMatchers(HttpMethod.POST, "api/v1/categories").hasRole(ROLE_MODERATOR)
                                .requestMatchers(HttpMethod.PUT, "api/v1/categories/**").hasRole(ROLE_MODERATOR)
                                .requestMatchers(HttpMethod.DELETE, "api/v1/categories/**").hasRole(ROLE_MODERATOR)
                                .requestMatchers(HttpMethod.POST, "api/v1/programming-languages").hasRole(ROLE_MODERATOR)
                                .requestMatchers(HttpMethod.PUT, "api/v1/programming-languages/**").hasRole(ROLE_MODERATOR)
                                .requestMatchers(HttpMethod.DELETE, "api/v1/programming-languages/**").hasRole(ROLE_MODERATOR)
                                .requestMatchers(HttpMethod.DELETE, "api/v1/users/*").hasRole(ROLE_ADMIN)
                                .requestMatchers(HttpMethod.PUT, "api/v1/users/*").hasRole(ROLE_ADMIN)
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(this.securityFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(c -> c.configurationSource(customCorsConfiguration))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

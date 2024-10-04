package com.elyashevich.mmfask.config;

import com.elyashevich.mmfask.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, "api/v1/auth/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/users/**").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "api/v1/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/posts/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/programming-languages/**").permitAll()
                                .requestMatchers("api/v1/posts/**").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "api/v1/categories").hasRole("MODERATOR")
                                .requestMatchers(HttpMethod.PUT, "api/v1/categories/**").hasRole("MODERATOR")
                                .requestMatchers(HttpMethod.DELETE, "api/v1/categories/**").hasRole("MODERATOR")
                                .requestMatchers(HttpMethod.POST, "api/v1/programming-languages").hasRole("MODERATOR")
                                .requestMatchers(HttpMethod.PUT, "api/v1/programming-languages/**").hasRole("MODERATOR")
                                .requestMatchers(HttpMethod.DELETE, "api/v1/programming-languages/**").hasRole("MODERATOR")
                                .requestMatchers(HttpMethod.DELETE, "api/v1/users/*").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "api/v1/users/*").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(this.securityFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
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

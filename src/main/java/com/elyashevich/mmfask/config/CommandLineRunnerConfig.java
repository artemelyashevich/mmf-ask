package com.elyashevich.mmfask.config;

import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CommandLineRunnerConfig {

    @Value("${application.admin.email}")
    private String adminEmail;

    @Value("${application.admin.password}")
    private String adminPassword;

    private final UserService userService;

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner commandLineRunnerBean() {
        return args -> {
            if (!this.userRepository.existsByEmail(adminEmail)) {
                var admin = User.builder()
                        .email(this.adminEmail)
                        .password(this.adminPassword)
                        .build();
                this.userService.createAdmin(admin);
            }
        };
    }
}

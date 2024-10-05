package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.service.AuthService;
import com.elyashevich.mmfask.service.converter.impl.UserConverter;
import com.elyashevich.mmfask.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String register(final AuthRequestDto authRequestDto) {
        log.debug("Attempting to register a new user with email '{}'.", authRequestDto.email());

        var candidate = this.userConverter.fromAuthDto(authRequestDto);
        this.userService.create(candidate);
        var user = this.userService.loadUserByUsername(authRequestDto.email());
        var token = generateToken(user);

        log.info("User with email '{}' has been registered.", authRequestDto.email());
        return token;
    }

    @Override
    public String login(final AuthRequestDto authRequestDto) {
        log.debug("Attempting to login user with email '{}'.", authRequestDto.email());

        var user = this.userService.loadUserByUsername(authRequestDto.email());
        if (!this.passwordEncoder.matches(authRequestDto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password.");
        }
        var token = generateToken(user);

        log.info("User with email '{}' has been logged in.", authRequestDto.email());
        return token;
    }

    private static String generateToken(final UserDetails userDetails) {
        return TokenUtil.generateToken(new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        ));
    }
}

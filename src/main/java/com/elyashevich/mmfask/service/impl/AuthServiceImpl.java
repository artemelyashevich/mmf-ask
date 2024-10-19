package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.service.AuthService;
import com.elyashevich.mmfask.service.MailService;
import com.elyashevich.mmfask.service.converter.impl.UserConverter;
import com.elyashevich.mmfask.util.TokenUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final MailService mailService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    private final static String PATH_TO_RESET_PASSWORD = "reset_password";
    private final static String PATH_TO_ACTIVATE_ACCOUNT = "activate_account";

    @Transactional
    @Override
    public void register(final AuthRequestDto authRequestDto) throws MessagingException {
        log.debug("Attempting to register a new user with email '{}'.", authRequestDto.email());

        var candidate = this.userConverter.fromAuthDto(authRequestDto);
        this.userService.create(candidate);
        var activationCode = generateActivationToken();
        candidate.setActivationCode(activationCode);
        this.mailService.sendMessage(authRequestDto.email(), activationCode, PATH_TO_ACTIVATE_ACCOUNT);

        log.info("User with email '{}' has been registered.", authRequestDto.email());
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

    @Override
    public String activateUser(final String email, final String code) {
        log.debug("Attempting to activate user with email '{}'.", email);

        var user = this.userService.findByEmail(email);
        if (!user.getActivationCode().equals(code)) {
            throw new InvalidTokenException("Invalid activation code.");
        }
        this.userService.activate(email);
        var userDetails = this.userService.loadUserByUsername(email);
        var token = generateToken(userDetails);

        log.info("User with email '{}' has been activated.", email);
        return token;
    }

    @Override
    public void sendResetPasswordCode(final String email) throws MessagingException {
        log.debug("Attempting to send reset password code to user with email '{}'", email);

        var resetCode = generateActivationToken();
        this.userService.setActivationCode(email, resetCode);
        this.mailService.sendMessage(email, resetCode, PATH_TO_RESET_PASSWORD);

        log.info("Reset password code gas been sent to user with email '{}'.", email);
    }

    @Override
    public String resetPassword(final String email, final String code, final ResetPasswordDto dto) {
        log.debug("Attempting to reset password of user with email '{}'", email);

        var user = this.userService.resetPassword(email, code, dto.oldPassword(), dto.newPassword());
        var token = generateToken(new User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList()
        ));

        log.info("Password of user with email '{}' has been reseted.", email);
        return token;
    }

    private static String generateToken(final UserDetails userDetails) {
        return TokenUtil.generateToken(new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        ));
    }

    private static String generateActivationToken() {
        var characters = "0123456789";
        var tokenBuilder = new StringBuilder();
        var random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));
        }
        return tokenBuilder.toString();
    }
}

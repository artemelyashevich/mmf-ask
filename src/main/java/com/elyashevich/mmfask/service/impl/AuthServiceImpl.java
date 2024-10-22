package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.service.ActivationCodeService;
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
    private final ActivationCodeService activationCodeService;

    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    private final static String PATH_TO_RESET_PASSWORD = "reset_password";
    private final static String PATH_TO_ACTIVATE_ACCOUNT = "activate_account";

    @Override
    public void activate(final String email) throws MessagingException {
        log.debug("Attempting to save activation code to user with email '{}'.", email);
        var activationCode = generateActivationToken();
        this.activationCodeService.create(activationCode, email);
        this.mailService.sendMessage(email, activationCode, PATH_TO_ACTIVATE_ACCOUNT);

        log.info("Activation code has been created to user with email {}.", email);
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

    @Transactional
    @Override
    public void activateUser(final String email, final String code) {
        log.debug("Attempting to activate user with email '{}'.", email);

        var activationCode = this.activationCodeService.findByEmail(email);
        if (!activationCode.getValue().equals(code)) {
            throw new InvalidTokenException("Invalid activation code.");
        }
        activationCode.setConfirmed(true);
        this.activationCodeService.setConfirmed(activationCode);
    }

    @Override
    public void sendResetPasswordCode(final String email) throws MessagingException {
        log.debug("Attempting to send reset password code to user with email '{}'", email);

        var resetCode = generateActivationToken();
        this.activationCodeService.update(email, resetCode);
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

    @Transactional
    @Override
    public String register(String email, AuthRequestDto dto) {
        if (!this.activationCodeService.findByEmail(email).isConfirmed()) {
            throw new InvalidTokenException("Code mismatch.");
        }
        var candidate = this.userConverter.fromAuthDto(new AuthRequestDto(email, dto.password()));
        this.userService.create(candidate);
        return generateToken(this.userService.loadUserByUsername(email));
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

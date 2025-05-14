package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import com.elyashevich.mmfask.entity.Role;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.AuthService;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class AuthServiceTest extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void login_ValidCredentials_ReturnsToken() {
        // Arrange
        var user = buildUser("1", "loginuser@example.com", passwordEncoder.encode("password"), Set.of(Role.ROLE_USER));
        userRepository.save(user);

        var authRequestDto = new AuthRequestDto("loginuser@example.com", "password");

        // Act
        var token = authService.login(authRequestDto);

        // Assert
        assertNotNull(token, "Token should be generated");
        assertTrue(TokenUtil.validate(token), "Token should be valid");
    }

    @Test
    void login_InvalidPassword_ThrowsInvalidPasswordException() {
        // Arrange
        var user = buildUser("1", "loginuser@example.com", passwordEncoder.encode("password"), Set.of(Role.ROLE_USER));
        userRepository.save(user);

        var authRequestDto = new AuthRequestDto("loginuser@example.com", "wrongpassword");

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> authService.login(authRequestDto));
    }

    @Test
    void activateUser_ValidCode_ActivatesUserAndReturnsToken() {
        // Arrange
        var user = buildUser("1", "activateuser@example.com", "password", Set.of(Role.ROLE_GUEST));
        user.setActivationCode("123456");
        userRepository.save(user);

        // Act
        var token = authService.activateUser("activateuser@example.com", "123456");

        // Assert
        var updatedUser = userRepository.findByEmail("activateuser@example.com").orElseThrow();
        assertAll(
                () -> assertTrue(updatedUser.getRoles().contains(Role.ROLE_USER), "User should be activated"),
                () -> assertNotNull(token, "Token should be generated"),
                () -> assertTrue(TokenUtil.validate(token), "Token should be valid")
        );
    }

    @Test
    void activateUser_InvalidCode_ThrowsInvalidTokenException() {
        // Arrange
        var user = buildUser("1", "activateuser@example.com", "password", Set.of(Role.ROLE_GUEST));
        user.setActivationCode("123456");
        userRepository.save(user);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> authService.activateUser("activateuser@example.com", "wrongcode"));
    }

    @Test
    void resetPassword_ValidInputs_ResetsPasswordAndReturnsToken() {
        // Arrange
        var user = buildUser("1", "resetuser@example.com", passwordEncoder.encode("oldPassword"), Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        var resetPasswordDto = new ResetPasswordDto("oldPassword", "newPassword");

        // Act
        var token = authService.resetPassword("resetuser@example.com", "123456", resetPasswordDto);

        // Assert
        var updatedUser = userRepository.findByEmail("resetuser@example.com").orElseThrow();
        assertAll(
                () -> assertTrue(passwordEncoder.matches("newPassword", updatedUser.getPassword()), "Password should be updated"),
                () -> assertNotNull(token, "Token should be generated"),
                () -> assertTrue(TokenUtil.validate(token), "Token should be valid")
        );
    }

    @Test
    void resetPassword_InvalidCode_ThrowsInvalidTokenException() {
        // Arrange
        var user = buildUser("1", "resetuser@example.com", passwordEncoder.encode("oldPassword"), Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        var resetPasswordDto = new ResetPasswordDto("oldPassword", "newPassword");

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> authService.resetPassword("resetuser@example.com", "wrongcode", resetPasswordDto));
    }

    @Test
    void resetPassword_InvalidOldPassword_ThrowsInvalidPasswordException() {
        // Arrange
        var user = buildUser("1", "resetuser@example.com", passwordEncoder.encode("oldPassword"), Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        var resetPasswordDto = new ResetPasswordDto("wrongPassword", "newPassword");

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> authService.resetPassword("resetuser@example.com", "123456", resetPasswordDto));
    }

    // Helper Method to Build User Entity
    private User buildUser(String id, String email, String password, Set<Role> roles) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .roles(roles)
                .build();
    }
}
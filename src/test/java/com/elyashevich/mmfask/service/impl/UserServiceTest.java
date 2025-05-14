package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.entity.Role;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceTest extends BaseIntegrationTest {

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
    void findById_ExistingUser_ReturnsUser() {
        // Arrange
        var user = buildUser("1", "test@example.com", "password", Set.of(Role.ROLE_USER));
        userRepository.save(user);

        // Act
        var result = userService.findById("1");

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("1", result.getId()),
                () -> assertEquals("test@example.com", result.getEmail())
        );
    }

    @Test
    void findById_NonExistingUser_ThrowsResourceNotFoundException() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.findById("nonexistent-id"));
    }

    @Test
    void create_ValidUser_CreatesUser() {
        // Arrange
        var user = buildUser(null, "new@example.com", "password", Set.of(Role.ROLE_GUEST));

        // Act
        var result = userService.create(user);

        // Assert
        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertEquals("new@example.com", result.getEmail()),
                () -> assertTrue(passwordEncoder.matches("password", result.getPassword())),
                () -> assertEquals(Set.of(Role.ROLE_GUEST), result.getRoles())
        );
    }

    @Test
    void create_DuplicateUser_ThrowsResourceAlreadyExistsException() {
        // Arrange
        var user = buildUser(null, "duplicate@example.com", "password", Set.of(Role.ROLE_GUEST));
        userRepository.save(user);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.create(user));
    }

    @Test
    void activate_ValidEmail_ActivatesUser() {
        // Arrange
        var user = buildUser("1", "activate@example.com", "password", Set.of(Role.ROLE_GUEST));
        userRepository.save(user);

        // Act
        var result = userService.activate("activate@example.com");

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.getRoles().contains(Role.ROLE_USER)),
                () -> assertEquals("activate@example.com", result.getEmail())
        );
    }

    @Test
    void activate_NonExistingEmail_ThrowsResourceNotFoundException() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.activate("nonexistent@example.com"));
    }

    @Test
    void resetPassword_ValidInputs_ResetsPassword() {
        // Arrange
        var user = buildUser("1", "reset@example.com", passwordEncoder.encode("oldPassword"), Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        // Act
        var result = userService.resetPassword("reset@example.com", "123456", "oldPassword", "newPassword");

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(passwordEncoder.matches("newPassword", result.getPassword()))
        );
    }

    @Test
    void resetPassword_InvalidCode_ThrowsInvalidTokenException() {
        // Arrange
        var user = buildUser("1", "reset@example.com", passwordEncoder.encode("oldPassword"), Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> userService.resetPassword("reset@example.com", "wrongCode", "oldPassword", "newPassword"));
    }

    @Test
    void resetPassword_InvalidOldPassword_ThrowsInvalidPasswordException() {
        // Arrange
        var user = buildUser("1", "reset@example.com", passwordEncoder.encode("oldPassword"), Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> userService.resetPassword("reset@example.com", "123456", "wrongPassword", "newPassword"));
    }

    @Test
    void updateEmail_ValidCode_UpdatesEmail() {
        // Arrange
        var user = buildUser("1", "old@example.com", "password", Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        var userDto = new UserDto(
                "1",
                "new@example.com",
                null,
                Map.of("articles", 5),
                List.of(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        // Act
        var result = userService.updateEmail("old@example.com", userDto, "123456");

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("new@example.com", result.getEmail(), "Email should be updated"),
                () -> assertEquals(userDto.badges(), result.getBadges(), "Badges should be updated")
        );
    }

    @Test
    void updateEmail_InvalidCode_ThrowsInvalidTokenException() {
        // Arrange
        var user = buildUser("1", "old@example.com", "password", Set.of(Role.ROLE_USER));
        user.setActivationCode("123456");
        userRepository.save(user);

        var userDto = new UserDto(
                "1",
                "new@example.com",
                null,
                Map.of("articles", 5),
                List.of(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> userService.updateEmail("old@example.com", userDto, "wrongCode"));
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
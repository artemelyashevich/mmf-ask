package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class UserConverterTest {

    private final UserConverter userConverter = new UserConverter();

    @ParameterizedTest
    @MethodSource("provideValidUsersForUpdate")
    void update_ValidUsers_UpdatesOldUserWithNewUserValues(User user, User newUser, String email, String password) {
        // Act
        var updatedUser = userConverter.update(user, newUser);

        // Assert
        assertAll(
                () -> assertEquals(email, updatedUser.getEmail()),
                () -> assertEquals(password, updatedUser.getPassword()),
                () -> assertSame(user, updatedUser)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUsersForUpdate")
    void update_InvalidUsers_ThrowsNullPointerException(User oldUser, User newUser) {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userConverter.update(oldUser, newUser));
    }

    @ParameterizedTest
    @MethodSource("provideAuthDtosForConversion")
    void fromAuthDto_ValidAuthRequestDto_ReturnsUser(AuthRequestDto requestDto, String email, String password) {
        // Act
        var user = userConverter.fromAuthDto(requestDto);

        // Assert
        assertAll(
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(password, user.getPassword())
        );
    }

    private static Stream<Arguments> provideValidUsersForUpdate() {
        // Arrange
        return Stream.of(
                Arguments.of(
                        buildUser("old@example.com", "oldPass"),
                        buildUser("new@example.com", "newPass"),
                        "new@example.com", "newPass"
                ),
                Arguments.of(
                        buildUser("old@example.com", "oldPass"),
                        buildUser(null, null),
                        null, null
                )
        );
    }

    private static Stream<Arguments> provideInvalidUsersForUpdate() {
        // Arrange
        return Stream.of(
                Arguments.of(null, buildUser("new@example.com", "newPass")),
                Arguments.of(buildUser("old@example.com", "oldPass"), null),
                Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> provideAuthDtosForConversion() {
        // Arrange
        return Stream.of(
                Arguments.of(
                        new AuthRequestDto("test@example.com", "password123"),
                        "test@example.com", "password123"
                ),
                Arguments.of(
                        new AuthRequestDto("user@example.com", "securePass"),
                        "user@example.com", "securePass"
                )
        );
    }

    private static User buildUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .roles(Set.of())
                .build();
    }
}
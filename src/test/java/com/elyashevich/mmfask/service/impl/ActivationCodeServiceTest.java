package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.ActivationCode;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ActivationCodeRepository;
import com.elyashevich.mmfask.service.ActivationCodeService;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class ActivationCodeServiceTest extends BaseIntegrationTest {

    @Autowired
    private ActivationCodeRepository repository;

    @Autowired
    private ActivationCodeService service;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("provideEmailsAndValuesForFindByEmail")
    void findByEmail_ExistingEmail_ReturnsActivationCode(String email, String value) {
        // Arrange
        repository.save(
                ActivationCode.builder()
                        .email(email)
                        .value(value)
                        .build()
        );

        // Act
        var result = service.findByEmail(email);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(email, result.getEmail()),
                () -> assertEquals(value, result.getValue())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingEmails")
    void findByEmail_NonExistingEmail_ThrowsResourceNotFoundException(String email) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.findByEmail(email));
    }

    @ParameterizedTest
    @MethodSource("provideEmailsAndValuesForCreate")
    void create_ValidData_SavesAndReturnsActivationCode(String email, String value) {
        // Act
        var result = service.create(value, email);

        // Assert
        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertEquals(email, result.getEmail()),
                () -> assertEquals(value, result.getValue())
        );
    }

    @ParameterizedTest
    @MethodSource("provideEmailsAndValuesForUpdate")
    void update_ExistingEmail_UpdatesCodeSuccessfully(String email, String oldValue, String newValue) {
        // Arrange
        repository.save(
                ActivationCode.builder()
                        .email(email)
                        .value(oldValue)
                        .build()
        );

        // Act
        service.update(email, newValue);

        // Assert
        var updatedCode = service.findByEmail(email);
        assertAll(
                () -> assertNotNull(updatedCode),
                () -> assertEquals(newValue, updatedCode.getValue())
        );
    }

    @ParameterizedTest
    @MethodSource("provideEmailsAndConfirmationStates")
    void setConfirmed_ExistingActivationCode_TogglesConfirmation(String email, boolean initialConfirmed) {
        // Arrange
        repository.save(
                ActivationCode.builder()
                        .email(email)
                        .value("code")
                        .isConfirmed(initialConfirmed)
                        .build()
        );

        // Act
        service.setConfirmed(service.findByEmail(email));

        // Assert
        var updatedCode = service.findByEmail(email);
        assertAll(
                () -> assertNotNull(updatedCode),
                () -> assertEquals(!initialConfirmed, updatedCode.isConfirmed())
        );
    }

    private static Stream<Arguments> provideEmailsAndValuesForFindByEmail() {
        return Stream.of(
                Arguments.of("test1@example.com", "123456"),
                Arguments.of("test2@example.com", "654321")
        );
    }

    private static Stream<Arguments> provideNonExistingEmails() {
        return Stream.of(
                Arguments.of("nonexistent1@example.com"),
                Arguments.of("nonexistent2@example.com")
        );
    }

    private static Stream<Arguments> provideEmailsAndValuesForCreate() {
        return Stream.of(
                Arguments.of("new1@example.com", "111111"),
                Arguments.of("new2@example.com", "222222")
        );
    }

    private static Stream<Arguments> provideEmailsAndValuesForUpdate() {
        return Stream.of(
                Arguments.of("update1@example.com", "oldCode1", "newCode1"),
                Arguments.of("update2@example.com", "oldCode2", "newCode2")
        );
    }

    private static Stream<Arguments> provideEmailsAndConfirmationStates() {
        return Stream.of(
                Arguments.of("confirm1@example.com", false),
                Arguments.of("confirm2@example.com", true)
        );
    }
}

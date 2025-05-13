package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class ProgrammingLanguageServiceTest extends BaseIntegrationTest {

    @Autowired
    private ProgrammingLanguageService programmingLanguageService;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @BeforeEach
    void setUp() {
        programmingLanguageRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("provideValidProgrammingLanguages")
    void create_ValidProgrammingLanguage_CreatesLanguage(final String name) {
        // Arrange
        var dto = buildProgrammingLanguageDto(name);

        // Act
        var createdLanguage = programmingLanguageService.create(dto);

        // Assert
        assertAll(
                () -> assertNotNull(createdLanguage.getId()),
                () -> assertEquals(name, createdLanguage.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideDuplicateProgrammingLanguages")
    void create_DuplicateProgrammingLanguage_ThrowsResourceAlreadyExistsException(final String name) {
        // Arrange
        var dto = buildProgrammingLanguageDto(name);
        programmingLanguageService.create(dto);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> programmingLanguageService.create(dto));
    }

    @ParameterizedTest
    @MethodSource("provideValidProgrammingLanguages")
    void findByName_ExistingName_ReturnsLanguage(final String name) {
        // Arrange
        var dto = buildProgrammingLanguageDto(name);
        programmingLanguageService.create(dto);

        // Act
        var foundLanguage = programmingLanguageService.findByName(name);

        // Assert
        assertAll(
                () -> assertNotNull(foundLanguage),
                () -> assertEquals(name, foundLanguage.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingProgrammingLanguages")
    void findByName_NonExistingName_ThrowsResourceNotFoundException(final String name) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> programmingLanguageService.findByName(name));
    }

    @ParameterizedTest
    @MethodSource("provideValidProgrammingLanguages")
    void findById_ExistingId_ReturnsLanguage(final String name) {
        // Arrange
        var dto = buildProgrammingLanguageDto(name);
        var createdLanguage = programmingLanguageService.create(dto);

        // Act
        var foundLanguage = programmingLanguageService.findById(createdLanguage.getId());

        // Assert
        assertAll(
                () -> assertNotNull(foundLanguage),
                () -> assertEquals(name, foundLanguage.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingProgrammingLanguageIds")
    void findById_NonExistingId_ThrowsResourceNotFoundException(final String id) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> programmingLanguageService.findById(id));
    }

    @ParameterizedTest
    @MethodSource("provideValidProgrammingLanguageUpdates")
    void update_ExistingLanguage_UpdatesLanguage(final String initialName, final String updatedName) {
        // Arrange
        var dto = buildProgrammingLanguageDto(initialName);
        var createdLanguage = programmingLanguageService.create(dto);
        var updateDto = buildProgrammingLanguageDto(updatedName);

        // Act
        var updatedLanguage = programmingLanguageService.update(createdLanguage.getId(), updateDto);

        // Assert
        assertAll(
                () -> assertEquals(updatedName, updatedLanguage.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidProgrammingLanguages")
    void delete_ExistingLanguage_DeletesLanguage(final String name) {
        // Arrange
        var dto = buildProgrammingLanguageDto(name);
        var createdLanguage = programmingLanguageService.create(dto);

        // Act
        programmingLanguageService.delete(createdLanguage.getId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> programmingLanguageService.findById(createdLanguage.getId()));
    }

    private ProgrammingLanguage buildProgrammingLanguageDto(final String name) {
        return ProgrammingLanguage.builder()
                .name(name)
                .build();
    }

    static Stream<Arguments> provideValidProgrammingLanguages() {
        return Stream.of(
                Arguments.of("Java"),
                Arguments.of("Python"),
                Arguments.of("JavaScript")
        );
    }

    static Stream<Arguments> provideDuplicateProgrammingLanguages() {
        return Stream.of(
                Arguments.of("Java"),
                Arguments.of("Python")
        );
    }

    static Stream<Arguments> provideNonExistingProgrammingLanguages() {
        return Stream.of(
                Arguments.of("Nonexistent Language 1"),
                Arguments.of("Nonexistent Language 2")
        );
    }

    static Stream<Arguments> provideNonExistingProgrammingLanguageIds() {
        return Stream.of(
                Arguments.of("nonexistent-id-1"),
                Arguments.of("nonexistent-id-2")
        );
    }

    static Stream<Arguments> provideValidProgrammingLanguageUpdates() {
        return Stream.of(
                Arguments.of("Java", "Java SE"),
                Arguments.of("Python", "Python 3")
        );
    }
}
package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class ProgrammingLanguageConverterTest {

    private final ProgrammingLanguageConverter programmingLanguageConverter = new ProgrammingLanguageConverter();

    @ParameterizedTest
    @MethodSource("provideValidEntitiesForUpdate")
    void update_ValidEntities(ProgrammingLanguage oldEntity, ProgrammingLanguage newEntity, String expectedName) {
        // Act
        var updatedEntity = programmingLanguageConverter.update(oldEntity, newEntity);

        // Assert
        assertAll(
                () -> assertEquals(expectedName, updatedEntity.getName(), "The name should be updated to the new entity's name"),
                () -> assertSame(oldEntity, updatedEntity, "The updated entity should be the same instance as the old entity")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEntitiesForUpdate")
    void update_ThrowsNullPointerException(ProgrammingLanguage oldEntity, ProgrammingLanguage newEntity) {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> programmingLanguageConverter.update(oldEntity, newEntity));
    }

    private static Stream<Arguments> provideValidEntitiesForUpdate() {
        // Arrange
        return Stream.of(
                Arguments.of(buildLanguage("Java"), buildLanguage("Python"), "Python"),
                Arguments.of(buildLanguage("Java"), buildLanguage(null), null)
        );
    }

    private static Stream<Arguments> provideInvalidEntitiesForUpdate() {
        // Arrange
        return Stream.of(
                Arguments.of(null, buildLanguage("Python")),
                Arguments.of(buildLanguage("Java"), null),
                Arguments.of(null, null)
        );
    }

    private static ProgrammingLanguage buildLanguage(final String name) {
        var language = new ProgrammingLanguage();
        language.setName(name);
        return language;
    }
}

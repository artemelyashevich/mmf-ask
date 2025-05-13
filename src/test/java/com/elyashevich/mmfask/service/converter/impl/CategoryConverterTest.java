package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.entity.Category;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class CategoryConverterTest {

    private final CategoryConverter categoryConverter = new CategoryConverter();

    @ParameterizedTest
    @MethodSource("provideValidEntitiesForUpdate")
    void update_ValidEntities_UpdatesOldEntityWithNewEntityValues(Category oldEntity, Category newEntity, String expectedName) {
        // Act
        var updatedEntity = categoryConverter.update(oldEntity, newEntity);

        // Assert
        assertAll(
                () -> assertEquals(expectedName, updatedEntity.getName(), "The name should be updated to the new entity's name"),
                () -> assertSame(oldEntity, updatedEntity, "The updated entity should be the same instance as the old entity")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEntitiesForUpdate")
    void update_InvalidEntities_ThrowsNullPointerException(Category oldEntity, Category newEntity) {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> categoryConverter.update(oldEntity, newEntity), "Updating with null entities should throw a NullPointerException");
    }

    private static Stream<Arguments> provideValidEntitiesForUpdate() {
        return Stream.of(
                Arguments.of(buildCategory("Old Name"), buildCategory("New Name"), "New Name"),
                Arguments.of(buildCategory("Old Name"), buildCategory(null), null),
                Arguments.of(buildCategory("Same Name"), buildCategory("Same Name"), "Same Name")
        );
    }

    private static Stream<Arguments> provideInvalidEntitiesForUpdate() {
        return Stream.of(
                Arguments.of(null, buildCategory("New Name")),
                Arguments.of(buildCategory("Old Name"), null),
                Arguments.of(null, null)
        );
    }

    private static Category buildCategory(String name) {
        var category = new Category();
        category.setName(name);
        return category;
    }
}
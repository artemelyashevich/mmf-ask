package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.CategoryRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class CategoryServiceTest extends BaseIntegrationTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    private static Category buildCategory(final String name) {
        return Category.builder()
                .name(name)
                .posts(List.of())
                .build();
    }

    @ParameterizedTest
    @MethodSource("provideCategoriesForFindByName")
    void findByName_ExistingName_ReturnsCategory(final String name) {
        // Arrange
        var category = buildCategory(name);
        categoryRepository.save(category);

        // Act
        var result = categoryService.findByName(name);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(name, result.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingNames")
    void findByName_NonExistingName_ThrowsResourceNotFoundException(final String name) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findByName(name));
    }

    @ParameterizedTest
    @MethodSource("provideCategoriesForCreate")
    void create_ValidCategory_CreatesCategory(final String name) {
        // Arrange
        var category = buildCategory(name);

        // Act
        var result = categoryService.create(category);

        // Assert
        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertEquals(name, result.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideDuplicateCategoriesForCreate")
    void create_DuplicateCategory_ThrowsResourceAlreadyExistsException(final String name) {
        // Arrange
        var category = buildCategory(name);
        categoryRepository.save(category);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> categoryService.create(category));
    }

    @ParameterizedTest
    @MethodSource("provideCategoriesForUpdate")
    void update_ExistingCategory_UpdatesCategory(final String initialName, final String updatedName) {
        // Arrange
        var category = buildCategory(initialName);
        var savedCategory = categoryRepository.save(category);

        var updatedCategory = buildCategory(updatedName);

        // Act
        var result = categoryService.update(savedCategory.getId(), updatedCategory);

        // Assert
        assertAll(
                () -> assertEquals(savedCategory.getId(), result.getId()),
                () -> assertEquals(updatedName, result.getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingIdsForUpdate")
    void update_NonExistingCategory_ThrowsResourceNotFoundException(final String id, final String name) {
        // Arrange
        var updatedCategory = buildCategory(name);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(id, updatedCategory));
    }

    @SuppressWarnings("all")
    @ParameterizedTest
    @MethodSource("provideCategoriesForDelete")
    void delete_ExistingCategory_RemovesCategory(final String name) {
        // Arrange
        var category = buildCategory(name);
        var savedCategory = categoryRepository.save(category);

        // Act
        categoryService.delete(savedCategory.getId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(savedCategory.getId()));
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingIdsForDelete")
    void delete_NonExistingCategory_ThrowsResourceNotFoundException(final String id) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(id));
    }

    static Stream<Arguments> provideCategoriesForFindByName() {
        return Stream.of(
                Arguments.of("Technology"),
                Arguments.of("Science"),
                Arguments.of("Art")
        );
    }

    static Stream<Arguments> provideNonExistingNames() {
        return Stream.of(
                Arguments.of("Nonexistent Category"),
                Arguments.of("Unknown Category")
        );
    }

    static Stream<Arguments> provideCategoriesForCreate() {
        return Stream.of(
                Arguments.of("Technology"),
                Arguments.of("Science"),
                Arguments.of("Art")
        );
    }

    static Stream<Arguments> provideDuplicateCategoriesForCreate() {
        return Stream.of(
                Arguments.of("Technology"),
                Arguments.of("Science")
        );
    }

    static Stream<Arguments> provideCategoriesForUpdate() {
        return Stream.of(
                Arguments.of("Technology", "Tech and Gadgets"),
                Arguments.of("Science", "Scientific Discoveries"),
                Arguments.of("Art", "Creative Arts")
        );
    }

    static Stream<Arguments> provideNonExistingIdsForUpdate() {
        return Stream.of(
                Arguments.of("nonexistent-id-1", "Updated Category 1"),
                Arguments.of("nonexistent-id-2", "Updated Category 2")
        );
    }

    static Stream<Arguments> provideCategoriesForDelete() {
        return Stream.of(
                Arguments.of("Technology"),
                Arguments.of("Science"),
                Arguments.of("Art")
        );
    }

    static Stream<Arguments> provideNonExistingIdsForDelete() {
        return Stream.of(
                Arguments.of("nonexistent-id-1"),
                Arguments.of("nonexistent-id-2")
        );
    }
}

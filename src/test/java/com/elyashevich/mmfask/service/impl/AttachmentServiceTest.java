package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.AttachmentImageRepository;
import com.elyashevich.mmfask.service.AttachmentService;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class AttachmentServiceTest extends BaseIntegrationTest {

    @Autowired
    private AttachmentImageRepository repository;

    @Autowired
    private AttachmentService service;

    @BeforeEach
    void setUp() {
        // Arrange: Clear the database before each test
        repository.deleteAll();
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideNamesAndFilesForFindByName")
    void findByName_ExistingName_ReturnsAttachmentImage(final String name, final MockMultipartFile file) {
        // Arrange
        var attachment = service.create(file);

        // Act
        var result = service.findByName(attachment.getFilename());

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.getFilename().contains(name)),
                () -> assertEquals(file.getContentType(), result.getFiletype()),
                () -> assertArrayEquals(file.getBytes(), result.getBytes())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingNames")
    void findByName_NonExistingName_ThrowsResourceNotFoundException(final String name) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.findByName(name));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideFilesForCreate")
    void create_ValidFile_SavesAndReturnsAttachmentImage(final MockMultipartFile file) {
        // Act
        var result = service.create(file);

        // Assert
        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertNotNull(result.getFilename()),
                () -> assertEquals(file.getContentType(), result.getFiletype()),
                () -> assertArrayEquals(file.getBytes(), result.getBytes())
        );
    }

    @SuppressWarnings("all")
    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideFilesForDelete")
    void delete_ExistingId_RemovesAttachmentImage(final MockMultipartFile file) {
        // Arrange
        var attachment = service.create(file);

        // Act
        service.delete(attachment.getId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> service.findById(attachment.getId()));
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingIds")
    void delete_NonExistingId_ThrowsResourceNotFoundException(final String id) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
    }

    private static Stream<Arguments> provideNamesAndFilesForFindByName() {
        // Arrange
        return Stream.of(
                Arguments.of("image1.png", new MockMultipartFile("image", "image1.png", "image/png", "image-content-1".getBytes())),
                Arguments.of("image2.jpg", new MockMultipartFile("image", "image2.jpg", "image/jpeg", "image-content-2".getBytes()))
        );
    }

    private static Stream<Arguments> provideNonExistingNames() {
        // Arrange
        return Stream.of(
                Arguments.of("nonexistent-image.png"),
                Arguments.of("nonexistent-image.jpg")
        );
    }

    private static Stream<Arguments> provideFilesForCreate() {
        // Arrange
        return Stream.of(
                Arguments.of(new MockMultipartFile("image", "image1.png", "image/png", "image-content-1".getBytes())),
                Arguments.of(new MockMultipartFile("image", "image2.jpg", "image/jpeg", "image-content-2".getBytes()))
        );
    }

    private static Stream<Arguments> provideFilesForDelete() {
        // Arrange
        return Stream.of(
                Arguments.of(new MockMultipartFile("image", "image1.png", "image/png", "image-content-1".getBytes())),
                Arguments.of(new MockMultipartFile("image", "image2.jpg", "image/jpeg", "image-content-2".getBytes()))
        );
    }

    private static Stream<Arguments> provideNonExistingIds() {
        // Arrange
        return Stream.of(
                Arguments.of("nonexistent-id-1"),
                Arguments.of("nonexistent-id-2")
        );
    }
}

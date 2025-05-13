package com.elyashevich.mmfask.util;

import com.elyashevich.mmfask.entity.AttachmentImage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.MockedStatic;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageUtilTest {

    private MockedStatic<ServletUriComponentsBuilder> mockedServletUriComponentsBuilder;


    @BeforeEach
    void setUp() {
        mockedServletUriComponentsBuilder = mockStatic(ServletUriComponentsBuilder.class);
    }

    @AfterEach
    void tearDown() {
        mockedServletUriComponentsBuilder.close();
    }

    @ParameterizedTest
    @CsvSource({
            "123, http://localhost/api/v1/images/123",
            "456, http://localhost/api/v1/images/456"
    })
    void generatePath_ValidImage_ReturnsExpectedPath(final String imageId, final String expectedPath) {
        // Arrange
        var image = mock(AttachmentImage.class);
        when(image.getId()).thenReturn(imageId);

        var mockBuilder = mock(ServletUriComponentsBuilder.class);
        mockedServletUriComponentsBuilder.when(ServletUriComponentsBuilder::fromCurrentContextPath).thenReturn(mockBuilder);
        when(mockBuilder.path("/api/v1/images/")).thenReturn(mockBuilder);
        when(mockBuilder.path(imageId)).thenReturn(mockBuilder);
        when(mockBuilder.toUriString()).thenReturn(expectedPath);

        // Act
        var actualPath = ImageUtil.generatePath(image);

        // Assert
        assertEquals(expectedPath, actualPath);
        verify(image, atLeastOnce()).getId();
    }

    @ParameterizedTest
    @NullSource
    void generatePath_NullImage_ReturnsNull(final AttachmentImage image) {
        // Act & Assert
        var actualPath = ImageUtil.generatePath(image);
        assertNull(actualPath);
    }
}
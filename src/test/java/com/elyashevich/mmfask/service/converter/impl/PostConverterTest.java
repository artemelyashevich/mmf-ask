package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class PostConverterTest {

    private final PostConverter postConverter = new PostConverter();

    @ParameterizedTest
    @MethodSource("provideValidEntitiesForUpdate")
    void update_ValidEntities(Post entity, Post newEntity, String title, String description, String language) {
        // Act
        var updatedEntity = postConverter.update(entity, newEntity);

        // Assert
        assertAll(
                () -> assertEquals(title, updatedEntity.getTitle()),
                () -> assertEquals(description, updatedEntity.getDescription()),
                () -> assertEquals(language, updatedEntity.getProgrammingLanguage().getName()),
                () -> assertSame(entity, updatedEntity)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEntitiesForUpdate")
    void update_InvalidEntities(Post oldEntity, Post newEntity) {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> postConverter.update(oldEntity, newEntity));
    }

    private static Stream<Arguments> provideValidEntitiesForUpdate() {
        return Stream.of(
                Arguments.of(
                        buildPost("Old Title", "Old Description", "Java"),
                        buildPost("New Title", "New Description", "Python"),
                        "New Title", "New Description", "Python"
                ),
                Arguments.of(
                        buildPost("Old Title", "Old Description", "Java"),
                        buildPost(null, null, null),
                        null, null, null, null
                )
        );
    }

    private static Stream<Arguments> provideInvalidEntitiesForUpdate() {
        return Stream.of(
                Arguments.of(null, buildPost("New Title", "New Description", "Python")),
                Arguments.of(buildPost("Old Title", "Old Description", "Java"), null),
                Arguments.of(null, null)
        );
    }

    private static Post buildPost(String title, String description, String language) {
        return Post.builder()
                .title(title)
                .description(description)
                .programmingLanguage(ProgrammingLanguage.builder().name(language).build())
                .categories(Set.of())
                .build();
    }
}
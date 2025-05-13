package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import com.elyashevich.mmfask.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class PostServiceTest extends BaseIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProgrammingLanguageService programmingLanguageService;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        programmingLanguageRepository.deleteAll();
        setupProgrammingLanguages();
        setupMockUser();
    }

    @ParameterizedTest
    @MethodSource("provideValidPostCreationData")
    @WithMockUser(username = "test-user")
    void create_ValidPost_CreatesPost(final String email, final String postTitle, final String programmingLanguage) {
        // Arrange
        var user = createTestUser(email);
        var dto = buildPostDto(postTitle, programmingLanguage, user);

        // Act
        var createdPost = postService.create(dto);

        // Assert
        assertAll(
                () -> assertNotNull(createdPost.getId()),
                () -> assertEquals(postTitle, createdPost.getTitle()),
                () -> assertEquals(programmingLanguage, createdPost.getProgrammingLanguage().getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingPostIds")
    @WithMockUser(username = "test-user")
    void findById_NonExistingId_ThrowsResourceNotFoundException(final String postId) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.findById(postId));
    }

    @ParameterizedTest
    @MethodSource("provideValidPostUpdateData")
    @WithMockUser(username = "test-user")
    void update_ExistingPost_UpdatesPost(final String email, final String initialTitle, final String updatedTitle, final String programmingLanguage) {
        // Arrange
        var user = createTestUser(email);
        var post = postService.create(buildPostDto(initialTitle, programmingLanguage, user));
        var updateDto = buildPostDto(updatedTitle, programmingLanguage, user);

        // Act
        var updatedPost = postService.update(post.getId(), updateDto);

        // Assert
        assertAll(
                () -> assertEquals(updatedTitle, updatedPost.getTitle()),
                () -> assertEquals(programmingLanguage, updatedPost.getProgrammingLanguage().getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideExistingPostTitles")
    @WithMockUser(username = "test-user")
    void findByName_ExistingTitle_ReturnsPost(final String email, final String postTitle, final String programmingLanguage) {
        // Arrange
        var user = createTestUser(email);
        postService.create(buildPostDto(postTitle, programmingLanguage, user));

        // Act
        var post = postService.findByName(postTitle);

        // Assert
        assertAll(
                () -> assertNotNull(post),
                () -> assertEquals(postTitle, post.getTitle()),
                () -> assertEquals(programmingLanguage, post.getProgrammingLanguage().getName())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingPostTitles")
    @WithMockUser(username = "test-user")
    void findByName_NonExistingTitle_ThrowsResourceNotFoundException(final String postTitle) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.findByName(postTitle));
    }

    @ParameterizedTest
    @MethodSource("provideValidPostIdsForDeletion")
    @WithMockUser(username = "test-user")
    void delete_ExistingPost_DeletesPost(final String email, final String postTitle, final String programmingLanguage) {
        // Arrange
        var user = createTestUser(email);
        var post = postService.create(buildPostDto(postTitle, programmingLanguage, user));

        // Act
        postService.delete(post.getId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.findById(post.getId()));
    }

    private User createTestUser(final String email) {
        return userService.create(
                User.builder()
                        .email(email)
                        .password("test-password")
                        .build()
        );
    }

    private Post buildPostDto(final String title, final String programmingLanguage, final User user) {
        var language = programmingLanguageService.findByName(programmingLanguage);
        return Post.builder()
                .title(title)
                .programmingLanguage(language)
                .categories(Set.of())
                .creator(user)
                .views(0L)
                .likes(0L)
                .dislikes(0L)
                .build();
    }

    private void setupProgrammingLanguages() {
        programmingLanguageService.create(
                ProgrammingLanguage.builder()
                        .name("Java")
                        .build()
        );
        programmingLanguageService.create(
                ProgrammingLanguage.builder()
                        .name("Python")
                        .build()
        );
    }

    private void setupMockUser() {
        userService.create(
                User.builder()
                        .email("test-user")
                        .password("mock-password")
                        .build()
        );
    }

    static Stream<Arguments> provideValidPostCreationData() {
        return Stream.of(
                Arguments.of("user1@example.com", "Post Title 1", "Java"),
                Arguments.of("user2@example.com", "Post Title 2", "Python")
        );
    }

    static Stream<Arguments> provideNonExistingPostIds() {
        return Stream.of(
                Arguments.of("nonexistent-id-1"),
                Arguments.of("nonexistent-id-2")
        );
    }

    static Stream<Arguments> provideValidPostUpdateData() {
        return Stream.of(
                Arguments.of("user1@example.com", "Initial Title 1", "Updated Title 1", "Java"),
                Arguments.of("user2@example.com", "Initial Title 2", "Updated Title 2", "Python")
        );
    }

    static Stream<Arguments> provideExistingPostTitles() {
        return Stream.of(
                Arguments.of("user1@example.com", "Post Title 1", "Java"),
                Arguments.of("user2@example.com", "Post Title 2", "Python")
        );
    }

    static Stream<Arguments> provideNonExistingPostTitles() {
        return Stream.of(
                Arguments.of("Nonexistent Title 1"),
                Arguments.of("Nonexistent Title 2")
        );
    }

    static Stream<Arguments> provideValidPostIdsForDeletion() {
        return Stream.of(
                Arguments.of("user1@example.com", "Post Title 1", "Java"),
                Arguments.of("user2@example.com", "Post Title 2", "Python")
        );
    }
}

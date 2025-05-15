package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Favorites;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.FavoritesRepository;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class FavoritesServiceTest extends BaseIntegrationTest {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private ProgrammingLanguageService programmingLanguageService;

    @BeforeEach
    void setUp() {
        favoritesRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
        programmingLanguageRepository.deleteAll();
        
        setupProgrammingLanguages();
    }

    @ParameterizedTest
    @MethodSource("provideExistingEmails")
    @WithMockUser(username = "user1@example.com")
    void findByUserEmail_ExistingEmail_ReturnsFavorites(final String email) {
        // Arrange
        var user = createTestUser(email);
        var post = createTestPost("Sample Post");
        createFavorites(user, post);

        // Act
        var favorites = favoritesService.findByUserEmail(email);

        // Assert
        assertAll(
                () -> assertNotNull(favorites),
                () -> assertEquals(email, favorites.getUser().getEmail()),
                () -> assertEquals(1, favorites.getPosts().size())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingEmails")
    @WithMockUser(username = "user1@example.com")
    void findByUserEmail_NonExistingEmail_ThrowsResourceNotFoundException(final String email) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoritesService.findByUserEmail(email));
    }

    @ParameterizedTest
    @MethodSource("provideValidPostCreationData")
    @WithMockUser(username = "user1@example.com")
    void create_ValidData_CreatesFavorites(final String email, final String postTitle) {
        // Arrange
        createTestUser(email);
        var post = createTestPost(postTitle);

        // Act
        var favorites = favoritesService.create(email, post.getId());

        // Assert
        assertAll(
                () -> assertNotNull(favorites.getId()),
                () -> assertEquals(email, favorites.getUser().getEmail()),
                () -> assertEquals(1, favorites.getPosts().size())
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPostRemovalData")
    @WithMockUser(username = "user1@example.com")
    void removePost_InvalidPost_ThrowsResourceNotFoundException(final String email, final String postId) {
        // Arrange
        createTestUser(email);
        createTestPost("Sample Post");

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoritesService.removePost(email, postId));
    }

    @ParameterizedTest
    @MethodSource("provideValidEmailsForDelete")
    @WithMockUser(username = "user1@example.com")
    void delete_ExistingFavorites_DeletesFavorites(final String email) {
        // Arrange
        var user = createTestUser(email);
        var post = createTestPost("Sample Post");
        createFavorites(user, post);

        // Act
        favoritesService.delete(email);

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> favoritesService.findByUserEmail(email));
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingEmailsForDelete")
    @WithMockUser(username = "user1@example.com")
    void delete_NonExistingFavorites_ThrowsResourceNotFoundException(final String email) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoritesService.delete(email));
    }

    private User createTestUser(final String email) {
        return userService.create(
                User.builder()
                        .email(email)
                        .password("test-password")
                        .build()
        );
    }

    private Post createTestPost(final String title) {
        var programmingLanguage = programmingLanguageService.findByName("Java");
        return postService.create(
                Post.builder()
                        .title(title)
                        .programmingLanguage(programmingLanguage)
                        .categories(Set.of())
                        .views(10L)
                        .build()
        );
    }

    private void createFavorites(final User user, final Post post) {
        favoritesRepository.save(Favorites.builder()
                .user(user)
                .posts(List.of(post))
                .build());
    }

    private void setupProgrammingLanguages() {
        programmingLanguageService.create(
                ProgrammingLanguage.builder()
                        .name("Java")
                        .build()
        );
    }

    static Stream<Arguments> provideExistingEmails() {
        return Stream.of(
                Arguments.of("user1@example.com"),
                Arguments.of("user1@example.com")
        );
    }

    static Stream<Arguments> provideNonExistingEmails() {
        return Stream.of(
                Arguments.of("nonexistent@example.com"),
                Arguments.of("unknown@example.com")
        );
    }

    static Stream<Arguments> provideValidPostCreationData() {
        return Stream.of(
                Arguments.of("user1@example.com", "Post Title 1"),
                Arguments.of("user1@example.com", "Post Title 2")
        );
    }

    static Stream<Arguments> provideDuplicatePostCreationData() {
        return Stream.of(
                Arguments.of("user1@example.com", "postId123"),
                Arguments.of("user1@example.com", "postId124")
        );
    }

    static Stream<Arguments> provideValidPostRemovalData() {
        return Stream.of(
                Arguments.of("user1@example.com", "Post Title 1"),
                Arguments.of("user1@example.com", "Post Title 2")
        );
    }

    static Stream<Arguments> provideInvalidPostRemovalData() {
        return Stream.of(
                Arguments.of("user1@example.com", "invalid-post-id-1"),
                Arguments.of("user1@example.com", "invalid-post-id-2")
        );
    }

    static Stream<Arguments> provideValidEmailsForDelete() {
        return Stream.of(
                Arguments.of("user1@example.com"),
                Arguments.of("user1@example.com")
        );
    }

    static Stream<Arguments> provideNonExistingEmailsForDelete() {
        return Stream.of(
                Arguments.of("nonexistent@example.com"),
                Arguments.of("unknown@example.com")
        );
    }
}
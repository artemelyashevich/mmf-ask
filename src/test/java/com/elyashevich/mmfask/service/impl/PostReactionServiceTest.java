package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.PostReaction;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.repository.PostReactionRepository;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class PostReactionServiceTest extends BaseIntegrationTest {

    @Autowired
    private PostReactionServiceImpl postReactionService;

    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void setUp() {
        postReactionRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        programmingLanguageRepository.deleteAll();

        setupProgrammingLanguages();
        prepareUser();
    }

    private void setupProgrammingLanguages() {
        programmingLanguageRepository.save(
                ProgrammingLanguage.builder()
                        .name("Java")
                        .build()
        );
    }

    private void prepareUser() {
        userRepository.save(
                User.builder()
                        .email("test-user")
                        .password("test-password")
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("provideToggleReactionData")
    @WithMockUser(username = "test-user")
    void toggleReaction_ToggleLikeAndDislike_UpdatesReaction(final String email, final String postTitle, final PostReaction.ReactionType initialType, final PostReaction.ReactionType newType, final PostReaction.ReactionType expectedType) {
        // Arrange
        var post = createTestPost(postTitle);
        createReactionIfNeeded(email, post.getId(), initialType);

        // Act
        var result = postReactionService.toggleReaction(email, post.getId(), newType);

        // Assert
        assertAll(
                () -> assertEquals(expectedType, result),
                () -> assertEquals(expectedType, findReactionType(email, post.getId()))
        );
    }

    @ParameterizedTest
    @MethodSource("provideReactionCountData")
    @WithMockUser(username = "test-user")
    void getReactionCounts_ValidPost_ReturnsCorrectCounts(final String postTitle, final long expectedLikes) {
        // Arrange
        var post = createTestPost(postTitle);
        createTestReaction("test-user", post.getId(), PostReaction.ReactionType.LIKE);

        // Act
        var result = postReactionService.getReactionCounts(post.getId());

        // Assert
        assertEquals(expectedLikes, result.get("likes"));
    }

    @ParameterizedTest
    @MethodSource("provideUserReactionData")
    @WithMockUser(username = "test-user")
    void getUserReaction_ValidPostAndUser_ReturnsReactionType(final String email, final String postTitle, final PostReaction.ReactionType reactionType) {
        // Arrange
        var post = createTestPost(postTitle);
        createTestReaction(email, post.getId(), reactionType);

        // Act
        var result = postReactionService.getUserReaction(email, post.getId());

        // Assert
        assertEquals(reactionType, result);
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingUserReactionData")
    @WithMockUser(username = "test-user")
    void getUserReaction_NonExistingUser_ReturnsNull(final String email, final String postTitle) {
        // Arrange
        var post = createTestPost(postTitle);

        // Act
        var result = postReactionService.getUserReaction(email, post.getId());

        // Assert
        assertNull(result);
    }

    private Post createTestPost(final String title) {
        var programmingLanguage = programmingLanguageRepository.findByName("Java");
        return postService.create(
                Post.builder()
                        .title(title)
                        .programmingLanguage(programmingLanguage.get())
                        .views(0L)
                        .categories(Set.of())
                        .build()
        );
    }

    private void createTestReaction(final String email, final String postId, final PostReaction.ReactionType type) {
        postReactionRepository.save(
                PostReaction.builder()
                        .email(email)
                        .postId(postId)
                        .type(type)
                        .build()
        );
    }

    private void createReactionIfNeeded(final String email, final String postId, final PostReaction.ReactionType type) {
        if (type != null) {
            createTestReaction(email, postId, type);
        }
    }

    private PostReaction.ReactionType findReactionType(final String email, final String postId) {
        return postReactionRepository.findByEmailAndPostId(email, postId)
                .map(PostReaction::getType)
                .orElse(null);
    }

    static Stream<Arguments> provideToggleReactionData() {
        return Stream.of(
                Arguments.of("test-user", "Post 1", null, PostReaction.ReactionType.LIKE, PostReaction.ReactionType.LIKE),
                Arguments.of("test-user", "Post 1", PostReaction.ReactionType.LIKE, PostReaction.ReactionType.DISLIKE, PostReaction.ReactionType.DISLIKE),
                Arguments.of("test-user", "Post 1", PostReaction.ReactionType.DISLIKE, PostReaction.ReactionType.LIKE, PostReaction.ReactionType.LIKE)
        );
    }

    static Stream<Arguments> provideReactionCountData() {
        return Stream.of(
                Arguments.of("Post 1", 1L),
                Arguments.of("Post 2", 1L)
        );
    }

    static Stream<Arguments> provideUserReactionData() {
        return Stream.of(
                Arguments.of("test-user", "Post 1", PostReaction.ReactionType.LIKE),
                Arguments.of("test-user", "Post 1", PostReaction.ReactionType.DISLIKE)
        );
    }

    static Stream<Arguments> provideNonExistingUserReactionData() {
        return Stream.of(
                Arguments.of("nonexistent@example.com", "Post 1"),
                Arguments.of("unknown@example.com", "Post 2")
        );
    }
}

package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.entity.Comment;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.repository.CommentRepository;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class CommentServiceTest extends BaseIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        var user = User.builder()
                .email("test@example.com")
                .build();
        userRepository.save(user);

        var post1 = Post.builder()
                .id("postId1")
                .title("Test Post")
                .description("Test Content")
                .views(1L)
                .build();
        postRepository.save(post1);

        var post2 = Post.builder()
                .id("postId2")
                .title("Test Post2")
                .description("Test Content")
                .views(10L)
                .build();
        postRepository.save(post2);
    }

    record CommentTestData(
            String email,
            String postId,
            String body,
            String updatedBody
    ) {}

    @ParameterizedTest
    @MethodSource("provideScenariosForCommentCreationAndUpdate")
    void createAndUpdateComment_ParameterizedTest(final CommentTestData testData) {
        // Arrange: Create a comment
        var post = postRepository.findById(testData.postId()).orElseThrow();
        var commentDto = new CommentRequestDto(post.getId(), testData.body());

        // Act: Create the comment
        var createdComment = commentService.create(commentDto, testData.email());
        assertNotNull(createdComment.getId(), "Created comment ID should not be null");

        // Act: Update the comment
        var updatedDto = new CommentRequestDto(post.getId(), testData.updatedBody());
        var updatedComment = commentService.update(createdComment.getId(), updatedDto);

        // Assert: Verify the updated comment
        assertAll(
                () -> assertEquals(createdComment.getId(), updatedComment.getId()),
                () -> assertEquals(testData.updatedBody(), updatedComment.getBody())
        );
    }

    @ParameterizedTest
    @MethodSource("provideScenariosForCommentLikesAndDislikes")
    void likeAndDislikeComment_ParameterizedTest(final CommentTestData testData) {
        // Arrange: Create a comment
        var user = userRepository.findByEmail(testData.email()).orElseThrow();
        var post = postRepository.findById(testData.postId()).orElseThrow();
        var comment = Comment.builder()
                .user(user)
                .post(post)
                .body(testData.body())
                .likes(0L)
                .dislikes(0L)
                .build();
        commentRepository.save(comment);

        // Act: Like the comment
        commentService.like(comment.getId());

        // Assert: Verify the likes count
        var likedComment = commentService.findById(comment.getId());
        assertEquals(1L, likedComment.getLikes(), "Comment likes should be incremented");

        // Act: Dislike the comment
        commentService.dislike(comment.getId());

        // Assert: Verify the dislikes count
        var dislikedComment = commentService.findById(comment.getId());
        assertEquals(1L, dislikedComment.getDislikes(), "Comment dislikes should be incremented");
    }

    static Stream<Arguments> provideScenariosForCommentCreationAndUpdate() {
        return Stream.of(
                Arguments.of(new CommentTestData("test@example.com", "postId1", "Original Body", "Updated Body")),
                Arguments.of(new CommentTestData("test@example.com", "postId2", "Another Body", "Another Updated Body"))
        );
    }

    static Stream<Arguments> provideScenariosForCommentLikesAndDislikes() {
        return Stream.of(
                Arguments.of(new CommentTestData("test@example.com", "postId1", "Original Body", null)),
                Arguments.of(new CommentTestData("test@example.com", "postId2", "Another Body", null))
        );
    }
}

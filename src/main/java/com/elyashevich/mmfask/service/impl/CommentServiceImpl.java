package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.entity.Comment;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.CommentRepository;
import com.elyashevich.mmfask.service.CommentService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    @Override
    public List<Comment> findAll(final String userId, final String postId) {
        return this.customFindAll(userId, postId);
    }

    @Override
    public Comment findById(final String id) {
        return this.commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id = %s was not found.".formatted(id)));
    }

    @Transactional
    @Override
    public Comment create(final CommentRequestDto dto, final String email) {
        log.debug("Attempting to create a new comment by user with email '{}'.", email);

        var user = this.userService.findByEmail(email);
        var post = this.postService.findById(dto.postId());
        var comment = Comment.builder()
                .user(user)
                .post(post)
                .body(dto.body())
                .build();
        var savedComment = this.commentRepository.save(comment);

        log.info("Comment by user with email '{}' has been created.", email);
        return savedComment;
    }

    @Transactional
    @Override
    public Comment update(final String id, final CommentRequestDto dto) {
        log.debug("Attempting to update a comment with id '{}'.", id);

        var comment = this.findById(id);
        comment.setBody(dto.body());
        var savedUser = this.commentRepository.save(comment);

        log.info("Comment with id '{}' has been updated.", id);
        return savedUser;
    }

    @Transactional
    @Override
    public void like(final String id) {
        var comment = this.findById(id);
        comment.setLikes(comment.getLikes() + 1);
        this.commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void undoLike(final String id) {
        var comment = this.findById(id);
        if (comment.getLikes() == 0) {
            throw new RuntimeException("");
        }
        comment.setDislikes(comment.getLikes() - 1);
        this.commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void dislike(final String id) {
        var comment = this.findById(id);
        comment.setDislikes(comment.getDislikes() + 1);
        this.commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void undoDislike(final String id) {
        var comment = this.findById(id);
        if (comment.getDislikes() == 0) {
            throw new RuntimeException("");
        }
        comment.setDislikes(comment.getDislikes() - 1);
        this.commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void delete(final String id) {
        log.debug("Attempting to delete a comment with id '{}'.", id);

        var comment = this.findById(id);
        this.commentRepository.delete(comment);

        log.info("Comment with id '{}' has been deleted.", id);
    }

    private List<Comment> customFindAll(final String userId, final String postId) {
        List<Comment> comments = new ArrayList<>();
        if (userId.isEmpty() && postId.isEmpty()) {
            comments = this.commentRepository.findAll();
        }
        if (!userId.isEmpty() && postId.isEmpty()) {
            comments = this.findAllByUserId(userId);
        }
        if (userId.isEmpty() && !postId.isEmpty()) {
            comments = this.findAllByPostId(postId);
        }
        if (!userId.isEmpty() && !postId.isEmpty()) {
            comments = this.findAllByUserAndPost(userId, postId);
        }
        return comments;
    }

    private List<Comment> findAllByUserId(final String id) {
        var user = this.userService.findById(id);
        return this.commentRepository.findAllByUser(user);
    }

    private List<Comment> findAllByPostId(final String id) {
        var post = this.postService.findById(id);
        return this.commentRepository.findAllByPost(post);
    }

    private List<Comment> findAllByUserAndPost(final String userId, final String postId) {
        var user = this.userService.findById(userId);
        var post = this.postService.findById(postId);
        return this.commentRepository.findAllByUserAndPost(user, post);
    }
}
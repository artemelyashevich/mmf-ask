package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.entity.Comment;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.CommentRepository;
import com.elyashevich.mmfask.service.CommentService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                .orElseThrow(()-> new ResourceNotFoundException("Comment with id = %s was not found.".formatted(id)));
    }

    @Transactional
    @Override
    public Comment create(final CommentRequestDto dto, final String email) {
        var user = this.userService.findByEmail(email);
        var post = this.postService.findById(dto.postId());
        var comment = Comment.builder()
                .user(user)
                .post(post)
                .body(dto.body())
                .build();
        return this.commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(final String id, final CommentRequestDto dto) {
        var comment = this.findById(id);
        comment.setBody(dto.body());
        return this.commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void delete(final String id) {
        var comment = this.findById(id);
        this.commentRepository.delete(comment);
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
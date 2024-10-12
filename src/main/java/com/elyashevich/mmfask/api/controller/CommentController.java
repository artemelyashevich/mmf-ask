package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.api.dto.comment.CommentResponseDto;
import com.elyashevich.mmfask.api.mapper.CommentMapper;
import com.elyashevich.mmfask.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping
    public List<CommentResponseDto> findAll(
            final @RequestParam(name = "postId", defaultValue = "", required = false) String postId,
            final @RequestParam(name = "userId", defaultValue = "", required = false) String userId
    ) {
        var comments = this.commentService.findAll(userId, postId);
        return this.commentMapper.toResponseDto(comments);
    }

    @GetMapping("/{commentId}")
    public CommentResponseDto findById(final @PathVariable("commentId") String id) {
        var comment = this.commentService.findById(id);
        return this.commentMapper.toResponseDto(comment);
    }

    @PostMapping
    public CommentResponseDto create(final @Validated CommentRequestDto dto, final Principal principal) {
        var email = principal.getName();
        var comment = this.commentService.create(dto, email);
        return commentMapper.toResponseDto(comment);
    }

    @PutMapping("/{commentId}")
    public CommentResponseDto update(
            final @PathVariable("commentId") String id,
            final @Validated CommentRequestDto dto
    ) {
        var comment = this.commentService.update(id, dto);
        return this.commentMapper.toResponseDto(comment);
    }

    @DeleteMapping("/{commentId}")
    public void delete(final @PathVariable("commentId") String id) {
        this.commentService.delete(id);
    }
}

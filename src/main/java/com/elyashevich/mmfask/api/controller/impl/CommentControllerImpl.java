package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.CommentController;
import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.api.dto.comment.CommentResponseDto;
import com.elyashevich.mmfask.api.mapper.CommentMapper;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import com.elyashevich.mmfask.service.BadgeAwardService;
import com.elyashevich.mmfask.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final BadgeAwardService badgeAwardService;

    @Override
    public List<CommentResponseDto> findAll(
            final @RequestParam(name = "postId", defaultValue = "", required = false) String postId,
            final @RequestParam(name = "userId", defaultValue = "", required = false) String userId
    ) {
        var comments = this.commentService.findAll(userId, postId);
        return this.commentMapper.toResponseDto(comments);
    }

    @Override
    public CommentResponseDto findById(final @PathVariable("commentId") String id) {
        var comment = this.commentService.findById(id);
        return this.commentMapper.toResponseDto(comment);
    }

    @Override
    public CommentResponseDto create(
            final @Validated @RequestBody CommentRequestDto dto,
            final @RequestParam("email") String email
    ) {
        var comment = this.commentService.create(dto, email);
        this.badgeAwardService.processAction(email, BadgeTriggerType.ANSWER_POSTED);
        return commentMapper.toResponseDto(comment);
    }

    @Override
    public void like(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.commentService.like(id);
    }

    @Override
    public void undoLike(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.commentService.undoDislike(id);
    }

    @Override
    public void dislike(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.commentService.dislike(id);
    }

    @Override
    public void undoDislike(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.commentService.undoDislike(id);
    }

    @Override
    public CommentResponseDto update(
            final @PathVariable("commentId") String id,
            final @Validated @RequestBody CommentRequestDto dto,
            final @RequestParam("email") String email
    ) {
        var comment = this.commentService.update(id, dto);
        return this.commentMapper.toResponseDto(comment);
    }

    @Override
    public void delete(final @PathVariable("commentId") String id, final @RequestParam("email") String email) {
        this.commentService.delete(id);
    }
}
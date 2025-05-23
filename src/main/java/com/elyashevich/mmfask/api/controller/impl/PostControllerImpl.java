package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.PostController;
import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.dto.post.PostStatisticsDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.entity.PostReaction;
import com.elyashevich.mmfask.service.PostReactionService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    private final PostService postService;
    private final StatisticService statisticService;
    private final PostMapper postMapper;
    private final PostReactionService postReactionService;

    @Override
    public Page<PostResponseDto> findAll(String query, Integer page, Integer size, String sortDirection, String sortField) {
        return this.postService.findAll(query, page, size, sortDirection, sortField).map(this.postMapper::toDto);
    }

    @Override
    public PostResponseDto findById(final @PathVariable("id") String id) {
        var post = this.postService.findById(id);
        return this.postMapper.toDto(post);
    }

    @Override
    public PostResponseDto create(final @RequestBody @Validated PostRequestDto dto) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var post = this.postService.create(this.postMapper.toEntity(dto));
        post.setAttachmentImages(new HashSet<>());
        return this.postMapper.toDto(post);
    }

    @Override
    public PostResponseDto uploadImage(
            final @PathVariable("id") String id,
            final @RequestParam("files") MultipartFile[] files,
            final @RequestParam("email") String email
    ) throws Exception {
        var post = this.postService.uploadFile(id, files);
        return this.postMapper.toDto(post);
    }

    @Override
    public PostResponseDto update(
            final @PathVariable("id") String id,
            final @RequestBody @Validated PostRequestDto dto,
            final @RequestParam("email") String email
    ) {
        var post = this.postService.update(id, postMapper.toEntity(dto));
        return this.postMapper.toDto(post);
    }

    @Override
    public void delete(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.postService.delete(id);
    }

    @Override
    public PostStatisticsDto findStatistics() {
        return this.statisticService.postStatistic();
    }

    @Override
    public ResponseEntity<?> toggleReaction(
            @PathVariable String postId,
            @RequestParam PostReaction.ReactionType type
    ) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var newState = postReactionService.toggleReaction(email, postId, type);
        var counts = postReactionService.getReactionCounts(postId);

        return ResponseEntity.ok(Map.of(
                "reaction", newState == null ? "NONE" : newState.name(),
                "likes", counts.get("likes"),
                "dislikes", counts.get("dislikes")
        ));
    }

    @Override
    public ResponseEntity<?> getReactionStatus(String postId) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var type = postReactionService.getUserReaction(email, postId);
        return ResponseEntity.ok(Map.of("reaction", type == null ? "NONE" : type.name()));
    }
}
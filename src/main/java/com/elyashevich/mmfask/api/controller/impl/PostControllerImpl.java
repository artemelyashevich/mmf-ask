package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.PostController;
import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.dto.post.PostStatisticsDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import com.elyashevich.mmfask.service.BadgeAwardService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    private final PostService postService;
    private final StatisticService statisticService;
    private final PostMapper postMapper;
    private final BadgeAwardService badgeAwardService;

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
    public void like(final @PathVariable("id") String id) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.badgeAwardService.processAction(email, BadgeTriggerType.LIKE_QUESTION);
        this.postService.like(id);
    }

    @Override
    public void undoLike(final @PathVariable("id") String id) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.badgeAwardService.processAction(email, BadgeTriggerType.UNLIKE_QUESTION);
        this.postService.undoLike(id);
    }

    @Override
    public void dislike(final @PathVariable("id") String id) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.badgeAwardService.processAction(email, BadgeTriggerType.DISLIKE_QUESTION);
        this.postService.dislike(id);
    }

    @Override
    public void undoDislike(final @PathVariable("id") String id) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.badgeAwardService.processAction(email, BadgeTriggerType.UNDISLIKE_QUESTION);
        this.postService.undoDislike(id);
    }

    @Override
    public PostResponseDto create(final @RequestBody @Validated PostRequestDto dto) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.badgeAwardService.processAction(email, BadgeTriggerType.QUESTION_POSTED);
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
}
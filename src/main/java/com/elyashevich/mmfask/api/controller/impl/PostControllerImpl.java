package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.PostController;
import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PostMapper postMapper;

    @Override
    public Page<PostResponseDto> findAll(
            final @RequestParam(value = "q", required = false, defaultValue = "") String query,
            final @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            final @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        return this.postService.findAll(query, Pageable.ofSize(size).withPage(page)).map(this.postMapper::toDto);
    }

    @Override
    public PostResponseDto findById(final @PathVariable("id") String id) {
        var post = this.postService.findById(id);
        return this.postMapper.toDto(post);
    }

    @Override
    public void like(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.postService.like(id);
    }

    @Override
    public void undoLike(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.postService.undoLike(id);
    }

    @Override
    public void dislike(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.postService.dislike(id);
    }

    @Override
    public void undoDislike(final @PathVariable("id") String id, final @RequestParam("email") String email) {
        this.postService.undoDislike(id);
    }

    @Override
    public PostResponseDto create(final @RequestBody @Validated PostRequestDto dto) {
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
}
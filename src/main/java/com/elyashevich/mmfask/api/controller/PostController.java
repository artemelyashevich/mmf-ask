package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public Page<PostResponseDto> findAll(
            final @RequestParam(value = "q", required = false, defaultValue = "") String query,
            final @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            final @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        return this.postService.findAll(
                        query, Pageable
                                .ofSize(size)
                                .withPage(page)
                ).map(this.postMapper::toDto);
    }

    @GetMapping("/{id}")
    public PostResponseDto findById(final @PathVariable("id") String id) {
        var post = this.postService.findById(id);
        return this.postMapper.toDto(post);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#dto.creatorEmail() == authentication.principal")
    public PostResponseDto create(final @Validated @RequestBody PostRequestDto dto) {
        var post = this.postService.create(this.postMapper.toEntity(dto));
        post.setAttachmentImages(new HashSet<>());
        return this.postMapper.toDto(post);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    public PostResponseDto uploadImage(
            final @PathVariable("id") String id,
            final @RequestParam("files") MultipartFile[] files,
            final @RequestParam("email") String email
    ) throws Exception {
        var post = this.postService.uploadFile(id, files);
        return this.postMapper.toDto(post);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#email == authentication.principal")
    public PostResponseDto update(
            final @PathVariable("id") String id,
            final @Validated @RequestBody PostRequestDto dto,
            final @RequestParam("email") String email
    ) {
        var post = this.postService.update(id, postMapper.toEntity(dto));
        return this.postMapper.toDto(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#email == authentication.principal")
    public void delete(final @PathVariable("id") String id, @RequestParam("email") String email) {
        this.postService.delete(id);
    }
}

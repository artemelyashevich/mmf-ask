package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public List<PostResponseDto> findAll() {
        var posts = this.postService.findAll();
        return this.postMapper.toDto(posts);
    }

    @GetMapping("/{id}")
    public PostResponseDto findById(final @PathVariable("id") String id) {
        var post = this.postService.findById(id);
        return this.postMapper.toDto(post);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto create(final @Validated @RequestBody PostRequestDto dto) {
        var post = this.postService.create(this.postMapper.toEntity(dto));
        post.setAttachmentImages(new HashSet<>());
        return this.postMapper.toDto(post);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto uploadImage(
            final @PathVariable("id") String id,
            final @RequestParam("files") MultipartFile[] files
    ) throws Exception {
        var post = this.postService.uploadFile(id, files);
        return this.postMapper.toDto(post);
    }

    @PutMapping("/{id}")
    public PostResponseDto update(
            final @PathVariable("id") String id,
            final @Validated @RequestBody PostRequestDto dto
    ) {
        var post = this.postService.update(id, postMapper.toEntity(dto));
        return this.postMapper.toDto(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final @PathVariable("id") String id) {
        this.postService.delete(id);
    }
}

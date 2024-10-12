package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.AttachmentImage;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.service.*;
import com.elyashevich.mmfask.service.converter.impl.PostConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter converter;

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProgrammingLanguageService programmingLanguageService;
    private final AttachmentService attachmentService;

    @Override
    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    @Override
    public Post findByName(final String name) {
        return this.postRepository.findByTitle(name)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post with name = %s was not found.".formatted(name))
                );
    }

    @Override
    public Post findById(final String id) {
        return this.postRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post with id = %s was not found.".formatted(id))
                );
    }

    @Transactional
    @Override
    public Post create(final Post dto) {
        log.debug("Attempting to create a new post with title '{}'.", dto.getTitle());

        dto.setAttachmentImages(new HashSet<>());
        var post = this.postRepository.save(this.setDataToDto(dto));

        log.info("Post with name '{}' has been created.", dto.getTitle());
        return post;
    }

    @Transactional
    @Override
    public Post update(final String id, final Post dto) {
        log.debug("Attempting to update a post with ID '{}'.", dto.getId());

        var candidate = this.findById(id);
        var post = this.converter.update(candidate, this.setDataToDto(dto));
        var updatedPost = this.postRepository.save(post);

        log.info("Post with ID '{}' has been updated.", dto.getId());
        return updatedPost;
    }

    @Transactional
    @Override
    public Post uploadFile(final String id, final MultipartFile[] files) throws Exception {
        log.debug("Attempting to upload image to post with ID '{}'.", id);

        var post = this.findById(id);
        var images = post.getAttachmentImages() != null
                ? post.getAttachmentImages()
                : new HashSet<AttachmentImage>();
        Arrays.stream(files).forEach(file -> {
            try {
                images.add(this.attachmentService.create(file));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Something went wrong.");
            }
        });
        post.setAttachmentImages(images);
        var updatedPost = this.postRepository.save(post);

        log.info("Post with ID '{}' has been updated.", id);
        return updatedPost;
    }

    @Transactional
    @Override
    public void delete(final String id) {
        log.debug("Attempting to delete a post with id '{}'.", id);

        var candidate = this.findById(id);
        this.postRepository.delete(candidate);

        log.info("Post with id '{}' has been deleted.", id);
    }

    @Transactional
    protected Post setDataToDto(final Post dto) {
        var categories = dto.getCategories().stream()
                .map(category -> this.categoryService.findByName(category.getName()))
                .collect(Collectors.toSet());
        var programmingLanguage = this.programmingLanguageService.findByName(dto.getProgrammingLanguage().getName());
        var user = this.userService.findByEmail(dto.getCreator().getEmail());
        dto.setCategories(categories);
        dto.setProgrammingLanguage(programmingLanguage);
        dto.setCreator(user);
        return dto;
    }
}

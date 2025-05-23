package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.AttachmentImage;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.service.AttachmentService;
import com.elyashevich.mmfask.service.BadgeAwardService;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.service.converter.impl.PostConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final BadgeAwardService badgeAwardService;

    @Override
    public Page<Post> findAll(String query, Integer page, Integer size, String sortDirection, String sortField) {
        log.debug("Attempting find All posts");
        var pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return this.postRepository.findByTitleContainingIgnoreCase(query, pageable);
    }

    @Override
    public Post findByName(final String name) {
        return this.postRepository.findByTitle(name)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post with name = %s was not found.".formatted(name))
                );
    }

    @Override
    @Transactional
    public Post findById(final String id) {
        var post = this.postRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Post with id = %s was not found.".formatted(id))
                );
        post.setViews(post.getViews() + 1);
        this.postRepository.save(post);
        return post;
    }

    @Transactional
    @Override
    public Post create(final Post dto) {
        log.debug("Attempting to create a new post with title '{}'.", dto.getTitle());

        dto.setAttachmentImages(new HashSet<>());
        var post = this.postRepository.save(this.setDataToDto(dto));
        this.badgeAwardService.processAction(post.getCreator().getId(), BadgeTriggerType.QUESTION_POSTED);
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
    public void like(final String id) {
        var post = this.findById(id);
        post.setLikes(post.getLikes() + 1);
        this.badgeAwardService.processAction(post.getCreator().getId(), BadgeTriggerType.LIKE_QUESTION);
        this.postRepository.save(post);
    }

    @Transactional
    @Override
    public void undoLike(final String id) {
        var post = this.findById(id);
        if (post.getLikes() == 0) {
            throw new RuntimeException("");
        }
        post.setLikes(post.getLikes() - 1);
        this.postRepository.save(post);
        this.badgeAwardService.processAction(post.getCreator().getId(), BadgeTriggerType.UNLIKE_QUESTION);
    }

    @Transactional
    @Override
    public void dislike(final String id) {
        var post = this.findById(id);
        post.setDislikes(post.getDislikes() + 1);
        this.badgeAwardService.processAction(post.getCreator().getId(), BadgeTriggerType.DISLIKE_QUESTION);
        this.postRepository.save(post);
    }

    @Transactional
    @Override
    public void undoDislike(final String id) {
        var post = this.findById(id);
        if (post.getDislikes() == 0) {
            throw new RuntimeException("");
        }
        post.setDislikes(post.getDislikes() - 1);
        this.badgeAwardService.processAction(post.getCreator().getId(), BadgeTriggerType.UNDISLIKE_QUESTION);
        this.postRepository.save(post);

    }

    @Override
    public List<Post> findAllByUserEmail(String userEmail) {
        return this.postRepository.findByCreator(this.userService.findByEmail(userEmail));
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
    public Post setDataToDto(final Post dto) {
        var categories = dto.getCategories().stream()
                .map(category -> this.categoryService.findByName(category.getName()))
                .collect(Collectors.toSet());
        var programmingLanguage = this.programmingLanguageService.findByName(dto.getProgrammingLanguage().getName());
        var user = this.userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        dto.setCategories(categories);
        dto.setProgrammingLanguage(programmingLanguage);
        dto.setCreator(user);
        dto.setLikes(0L);
        dto.setViews(0L);
        dto.setDislikes(0L);
        return dto;
    }
}
package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import com.elyashevich.mmfask.service.converter.impl.PostConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter converter;
    private final CategoryService categoryService;
    private final ProgrammingLanguageService programmingLanguageService;

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
        var categories = dto.getCategories().stream()
                .map(category -> this.categoryService.findByName(category.getName()))
                        .collect(Collectors.toSet());
        var programmingLanguage = this.programmingLanguageService.findByName(dto.getProgrammingLanguage().getName());
        dto.setCategories(categories);
        dto.setProgrammingLanguage(programmingLanguage);
        return this.postRepository.save(dto);
    }

    @Transactional
    @Override
    public Post update(final String id, final Post dto) {
        var candidate = this.findById(id);
        var categories = dto.getCategories().stream()
                .map(category -> this.categoryService.findByName(category.getName()))
                .collect(Collectors.toSet());
        var programmingLanguage = this.programmingLanguageService.findByName(dto.getProgrammingLanguage().getName());
        dto.setCategories(categories);
        dto.setProgrammingLanguage(programmingLanguage);
        var post = this.converter.update(candidate, dto);
        return this.postRepository.save(post);
    }

    @Transactional
    @Override
    public void delete(final String id) {
        var candidate = this.findById(id);
        this.postRepository.delete(candidate);
    }
}

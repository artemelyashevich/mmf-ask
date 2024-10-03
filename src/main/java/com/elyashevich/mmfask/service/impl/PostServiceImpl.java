package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.converter.impl.PostConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter converter;

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

    @Override
    public Post create(final Post dto) {
        return this.postRepository.save(dto);
    }

    @Transactional
    @Override
    public Post update(final String id, final Post dto) {
        var candidate = this.findById(id);
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

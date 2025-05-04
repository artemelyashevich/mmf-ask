package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.Resume;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ResumeRepository;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import com.elyashevich.mmfask.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CategoryService categoryService;
    private final ProgrammingLanguageService programmingLanguageService;
    private final PostService postService;

    @Override
    public Resume findById(String id) {
        log.debug("Attempting find resume by id: '{}'", id);

        var resume = this.resumeRepository.findById(id).orElseThrow(
                () -> {
                    var message = "Resume with id: '%s' was not found".formatted(id);
                    log.warn(message);
                    return new ResourceNotFoundException(message);
                }
        );

        log.info("Resume with id: '{}' found", id);
        return resume;
    }

    @Override
    @Transactional
    public Resume create(Resume entity) {
        log.debug("Attempting create new resume: {}", entity);

        var categories = entity.getCategories().stream()
                .map(it -> this.categoryService.findById(it.getId()))
                .collect(Collectors.toSet());
        var programingLanguages = entity.getProgrammingLanguages().stream()
                .map(it -> this.programmingLanguageService.findById(it.getId()))
                .collect(Collectors.toSet());
        var level = (Long)this.postService.findAllByUserEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .stream()
                .map(Post::getViews)
                .reduce(Long::sum)
                .orElse(0L) % 10;
        entity.setLevel(Integer.valueOf(Long.toString(level)));
        entity.setCategories(categories);
        entity.setProgrammingLanguages(programingLanguages);

        var newResume = this.resumeRepository.save(entity);

        log.info("Resume created: {}", newResume);
        return newResume;
    }

    @Override
    @Transactional
    public Resume update(String id, Resume entity) {
        log.debug("Attempting update resume: {}", entity);

        var oldResume = this.findById(id);

        var categories = entity.getCategories().stream()
                .map(it -> this.categoryService.findById(it.getId()))
                .collect(Collectors.toSet());
        var programingLanguages = entity.getProgrammingLanguages().stream()
                .map(it -> this.programmingLanguageService.findById(it.getId()))
                .collect(Collectors.toSet());

        oldResume.setExperienceInYears(entity.getExperienceInYears());
        oldResume.setLevel(entity.getLevel());
        oldResume.setCategories(categories);
        oldResume.setProgrammingLanguages(programingLanguages);

        var newResume = this.resumeRepository.save(oldResume);

        log.info("Resume updated: {}", newResume);
        return newResume;
    }

    @Override
    @Transactional
    public void delete(String id) {
        log.debug("Attempting delete resume by id: {}", id);

        var oldResume = this.findById(id);

        this.resumeRepository.delete(oldResume);
        log.info("Resume deleted: {}", id);
    }

    @Override
    public Resume findByUserId(String userId) {
        log.debug("Attempting find resume by userId: {}", userId);

        var resume = this.resumeRepository.findByUserId(userId).orElseThrow(
                () -> {
                    var message = "Resume with userId: '%s' was nor found".formatted(userId);
                    log.warn(message);
                    return new ResourceNotFoundException(message);
                }
        );

        log.info("Resume found by userId: {}", resume);
        return resume;
    }
}

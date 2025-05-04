package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.resume.ResumeCreateDto;
import com.elyashevich.mmfask.api.dto.resume.ResumeResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/resumes")
public interface ResumeController {

    @GetMapping("/{id}")
    ResumeResponseDto findById(@PathVariable("id") String id);

    @GetMapping("/user/{userId}")
    ResumeResponseDto findByUserId(@PathVariable("userId") String userId);

    @PostMapping
    ResumeResponseDto create(@Valid @RequestBody ResumeCreateDto dto);

    @PutMapping("/{id}")
    ResumeResponseDto update(@PathVariable("id") String id, @Valid @RequestBody ResumeCreateDto dto);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id);
}

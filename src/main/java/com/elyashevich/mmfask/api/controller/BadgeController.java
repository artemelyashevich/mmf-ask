package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.badge.BadgeCreateDto;
import com.elyashevich.mmfask.entity.Badge;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/badges")
public interface BadgeController {

    @GetMapping
    List<Badge> findAll();

    @PostMapping
    Badge create(@Valid @RequestBody BadgeCreateDto dto);
}

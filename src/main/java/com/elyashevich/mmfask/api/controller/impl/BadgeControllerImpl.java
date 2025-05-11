package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.BadgeController;
import com.elyashevich.mmfask.api.dto.badge.BadgeCreateDto;
import com.elyashevich.mmfask.api.mapper.BadgeMapper;
import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BadgeControllerImpl implements BadgeController {

    private final BadgeService badgeService;
    private final BadgeMapper badgeMapper;

    @Override
    public List<Badge> findAll() {
        return this.badgeService.findAll();
    }

    @Override
    public Badge create(BadgeCreateDto dto) {
        return this.badgeService.create(this.badgeMapper.toEntity(dto));
    }
}

package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.badge.BadgeCreateDto;
import com.elyashevich.mmfask.api.mapper.BadgeMapper;
import com.elyashevich.mmfask.entity.Badge;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BadgeMapperImpl implements BadgeMapper {

    @Override
    public BadgeCreateDto toDto(Badge entity) {
        return new BadgeCreateDto(
                entity.getName(),
                entity.getDescription(),
                entity.getIconUrl(),
                entity.getTriggerType(),
                entity.getConditionValue()
        );
    }

    @Override
    public List<BadgeCreateDto> toDto(List<Badge> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public Badge toEntity(BadgeCreateDto dto) {
        return Badge.builder()
                .name(dto.name())
                .description(dto.description())
                .conditionValue(dto.conditionValue())
                .triggerType(dto.triggerType())
                .build();
    }
}

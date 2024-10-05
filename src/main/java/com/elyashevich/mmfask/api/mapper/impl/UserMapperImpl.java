package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(final User entity) {
        var imagePath = entity.getImage() != null
                ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/images/")
                .path(entity.getImage().getId())
                .toUriString()
                : null;
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                imagePath,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    @Override
    public List<UserDto> toDto(final List<User> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public User toEntity(final UserDto dto) {
        return User.builder()
                .email(dto.email())
                .build();
    }
}

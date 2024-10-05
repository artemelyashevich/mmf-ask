package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.util.ImageUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(final User entity) {
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                ImageUtil.generatePath(entity.getImage()),
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

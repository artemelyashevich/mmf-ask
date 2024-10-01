package com.elyashevich.mmfask.service.converter;

import com.elyashevich.mmfask.api.dto.AuthRequestDto;
import com.elyashevich.mmfask.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User update(User oldUser) {
        return User.builder()
                .email(oldUser.getEmail())
                .build();
    }

    public User fromAuthDto(AuthRequestDto authRequestDto) {
        return User.builder()
                .email(authRequestDto.email())
                .password(authRequestDto.password())
                .build();
    }
}

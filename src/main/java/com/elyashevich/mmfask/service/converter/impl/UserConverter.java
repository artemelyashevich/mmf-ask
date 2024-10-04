package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.service.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User> {

    @Override
    public User update(User oldUser, User newUser) {
        oldUser.setPassword(newUser.getPassword());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setRoles(newUser.getRoles());
        return oldUser;
    }

    public User fromAuthDto(AuthRequestDto authRequestDto) {
        return User.builder()
                .email(authRequestDto.email())
                .password(authRequestDto.password())
                .build();
    }
}

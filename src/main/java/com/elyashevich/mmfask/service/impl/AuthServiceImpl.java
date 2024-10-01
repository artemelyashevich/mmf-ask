package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.AuthRequestDto;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.service.AuthService;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserConverter userConverter;

    @Override
    public User register(AuthRequestDto authRequestDto) {
        var candidate = this.userConverter.fromAuthDto(authRequestDto);
        return this.userService.create(candidate);
    }
}

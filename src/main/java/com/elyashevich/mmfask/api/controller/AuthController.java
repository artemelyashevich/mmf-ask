package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.UserDto;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public UserDto create(final @RequestBody AuthRequestDto authRequestDto){
        var user = this.authService.register(authRequestDto);
        System.out.println(user);
        return this.userMapper.toDto(user);
    }
}

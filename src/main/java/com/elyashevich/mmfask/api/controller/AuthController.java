package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.AuthResponseDto;
import com.elyashevich.mmfask.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto create(final @RequestBody AuthRequestDto authRequestDto){
        var token = this.authService.register(authRequestDto);
        return new AuthResponseDto(token);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto login(final @RequestBody AuthRequestDto authRequestDto){
        var token = this.authService.login(authRequestDto);
        return new AuthResponseDto(token);
    }
}

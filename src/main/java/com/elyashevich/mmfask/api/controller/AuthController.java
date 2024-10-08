package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.AuthResponseDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import com.elyashevich.mmfask.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(final @Validated @RequestBody AuthRequestDto authRequestDto) throws MessagingException {
        this.authService.register(authRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto login(final @Validated @RequestBody AuthRequestDto authRequestDto) {
        var token = this.authService.login(authRequestDto);
        return new AuthResponseDto(token);
    }

    @PostMapping("/activate/{email}")
    public AuthResponseDto activate(final @PathVariable("email") String email, final @RequestParam("code") String code) {
        var token = this.authService.activateUser(email, code);
        return new AuthResponseDto(token);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void resetPasswordCode() throws MessagingException {
        this.authService.sendResetPasswordCode();
    }

    @PostMapping("/reset-password/{code}")
    public AuthResponseDto resetPassword(
            final @PathVariable("code") String code,
            final @Validated @RequestBody ResetPasswordDto dto
    ) {
        var token = this.authService.resetPassword(code, dto);
        return new AuthResponseDto(token);
    }
}

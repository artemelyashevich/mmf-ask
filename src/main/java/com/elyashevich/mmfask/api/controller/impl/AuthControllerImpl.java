package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.AuthController;
import com.elyashevich.mmfask.api.dto.auth.AuthRequestDto;
import com.elyashevich.mmfask.api.dto.auth.AuthResponseDto;
import com.elyashevich.mmfask.api.dto.auth.ResetPasswordDto;
import com.elyashevich.mmfask.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public void create(final @Validated @RequestBody AuthRequestDto authRequestDto) throws MessagingException {
        this.authService.register(authRequestDto);
    }

    @Override
    public AuthResponseDto login(final @Validated @RequestBody AuthRequestDto authRequestDto) {
        var token = this.authService.login(authRequestDto);
        return new AuthResponseDto(token);
    }

    @Override
    public AuthResponseDto activate(
            final @PathVariable("email") String email, final @RequestParam("code") String code) {
        var token = this.authService.activateUser(email, code);
        return new AuthResponseDto(token);
    }

    @Override
    public void resetPasswordCode(final Principal principal) throws MessagingException {
        this.authService.sendResetPasswordCode(principal.getName());
    }

    @Override
    public AuthResponseDto resetPassword(
            final @PathVariable("code") String code,
            final @Validated @RequestBody ResetPasswordDto dto,
            final Principal principal
    ) {
        var token = this.authService.resetPassword(principal.getName(), code, dto);
        return new AuthResponseDto(token);
    }
}
package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.AuthController;
import com.elyashevich.mmfask.api.dto.auth.ActivationDto;
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
public class AAuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public void activate(final @Validated @RequestBody ActivationDto dto) throws MessagingException {
        this.authService.activate(dto.email());
    }

    @Override
    public void activation(
            final @PathVariable("email") String email, final @RequestParam("code") String code
    ) {
        this.authService.activateUser(email, code);
    }

    @Override
    public AuthResponseDto register(final String email, final AuthRequestDto dto) {
        var token = this.authService.register(email, dto);
        return new AuthResponseDto(token);
    }

    @Override
    public AuthResponseDto login(final @Validated @RequestBody AuthRequestDto authRequestDto) {
        var token = this.authService.login(authRequestDto);
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
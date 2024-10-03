package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.AuthRequestDto;
import com.elyashevich.mmfask.entity.Role;
import com.elyashevich.mmfask.exception.InvalidPasswordException;
import com.elyashevich.mmfask.service.AuthService;
import com.elyashevich.mmfask.service.UserService;
import com.elyashevich.mmfask.service.converter.UserConverter;
import com.elyashevich.mmfask.util.RoleUtil;
import com.elyashevich.mmfask.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(final AuthRequestDto authRequestDto) {
        var candidate = this.userConverter.fromAuthDto(authRequestDto);
        this.userService.create(candidate);
        var user = this.userService.loadUserByUsername(authRequestDto.email());
        return generateToken(user);
    }

    @Override
    public String login(final AuthRequestDto authRequestDto) {
        var user = this.userService.loadUserByUsername(authRequestDto.email());
        if (!this.passwordEncoder.matches(authRequestDto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password.");
        }
        return generateToken(user);
    }

    private static String generateToken(final UserDetails userDetails) {
        return TokenUtil.generateToken(new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        ));
    }
}

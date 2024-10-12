package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAll() {
        var users = this.userService.findAll();
        return this.userMapper.toDto(users);
    }

    @GetMapping("/{id}")
    public UserDto findById(final @PathVariable("id") String id) {
        var user = this.userService.findById(id);
        return this.userMapper.toDto(user);
    }

    @PostMapping("/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    public UserDto uploadImage(
            final @PathVariable("email") String email,
            final @RequestParam("file") MultipartFile file
    ) throws Exception {
        var user = this.userService.uploadImage(email, file);
        return this.userMapper.toDto(user);
    }
}

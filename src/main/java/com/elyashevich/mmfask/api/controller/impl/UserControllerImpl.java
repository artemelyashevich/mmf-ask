package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.UserController;
import com.elyashevich.mmfask.api.dto.user.UserDto;
import com.elyashevich.mmfask.api.dto.user.UserStatisticsDto;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.service.StatisticService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final StatisticService statisticService;

    @Override
    public List<UserDto> findAll() {
        var users = this.userService.findAll();
        return this.userMapper.toDto(users);
    }

    @Override
    public UserDto findById(final @PathVariable("id") String id) {
        var user = this.userService.findById(id);
        return this.userMapper.toDto(user);
    }

    @Override
    public UserStatisticsDto findStatistics() {
        return this.statisticService.userStatistic();
    }

    @Override
    public UserDto uploadImage(
            final @PathVariable("email") String email,
            final @RequestParam("file") MultipartFile file
    ) throws Exception {
        var user = this.userService.uploadImage(email, file);
        return this.userMapper.toDto(user);
    }
}
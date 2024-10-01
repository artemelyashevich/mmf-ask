package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(final String id);

    User create(final User user);
}

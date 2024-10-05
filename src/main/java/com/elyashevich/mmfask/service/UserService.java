package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(final String id);

    User create(final User user);

    User findByEmail(final String email);

    User uploadImage(final String id, final MultipartFile file) throws Exception;
}

package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    List<User> findAll();

    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user to find
     * @return the found user
     */
    User findById(final String id);

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    User create(final User user);

    /**
     * Finds a user by email.
     *
     * @param email the email of the user to find
     * @return the found user
     */
    User findByEmail(final String email);

    User activate(final String email);

    /**
     * Uploads an image for a specific user.
     *
     * @param id the ID of the user to upload the image for
     * @param file the multipart file representing the image to upload
     * @return the user with the uploaded image
     * @throws Exception if an error occurs during image upload
     */
    User uploadImage(final String id, final MultipartFile file) throws Exception;

    User resetPassword(final String email, final String code, final String oldPassword, final String newPassword);

    void setActivationCode(final String email, final String resetCode);
}
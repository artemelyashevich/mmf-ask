package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.service.contract.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing posts.
 */
public interface PostService extends CrudService<Post> {

    /**
     * Retrieves all posts.
     *
     * @return a list of all posts
     */
    Page<Post> findAll(final String query, final Pageable page);

    /**
     * Finds a post by name.
     *
     * @param name the name of the post to find
     * @return the found post
     */
    Post findByName(final String name);

    /**
     * <<<<<<< HEAD
     * =======
     * Finds a post by ID.
     *
     * @param id the ID of the post to find
     * @return the found post
     */
    Post findById(final String id);

    /**
     * Creates a new post.
     *
     * @param dto the post DTO to create
     * @return the created post
     */
    Post create(final Post dto);

    /**
     * Updates a post by ID.
     *
     * @param id  the ID of the post to update
     * @param dto the updated post DTO
     * @return the updated post
     */
    Post update(final String id, final Post dto);

    /**
     * Deletes a post by ID.
     *
     * @param id the ID of the post to delete
     */
    void delete(final String id);

    /**
     * Uploads a file for a specific post.
     *
     * @param id    the ID of the post to upload the file for
     * @param files the multipart file to upload
     * @return the updated post with the uploaded file
     * @throws Exception if an error occurs during file upload
     */
    Post uploadFile(final String id, final MultipartFile[] files) throws Exception;

    void like(final String id);

    void undoLike(final String id);

    void dislike(final String id);

    void undoDislike(final String id);

    List<Post> findAllByUserEmail(String userEmail);
}

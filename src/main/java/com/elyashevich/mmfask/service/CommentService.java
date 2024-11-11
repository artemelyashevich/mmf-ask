package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.comment.CommentRequestDto;
import com.elyashevich.mmfask.entity.Comment;

import java.util.List;

/**
 * The interface for managing user comments.
 */
public interface CommentService {

    /**
     * Retrieves all comments for a specific user and post.
     *
     * @param userId The ID of the user.
     * @param postId The ID of the post.
     * @return A list of comments for the specified user and post.
     */
    List<Comment> findAll(final String userId, final String postId);

    /**
     * Finds a comment by its ID.
     *
     * @param id The ID of the comment to find.
     * @return The comment with the specified ID.
     */
    Comment findById(final String id);

    /**
     * Creates a new comment using the provided data and user email.
     *
     * @param dto The data for creating the comment.
     * @param email The email of the user creating the comment.
     * @return The created Comment object.
     */
    Comment create(final CommentRequestDto dto, final String email);

    /**
     * Updates a comment with the provided data.
     *
     * @param id The ID of the comment to update.
     * @param dto The updated data for the comment.
     * @return The updated Comment object.
     */
    Comment update(final String id, final CommentRequestDto dto);

    void like(final String id);

    void undoLike(final String id);

    void dislike(final String id);

    void undoDislike(final String id);

    /**
     * Deletes a comment by its ID.
     *
     * @param id The ID of the comment to delete.
     */
    void delete(final String id);
}

package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Favorites;

import java.util.List;

/**
 * The interface for managing user favorites.
 */
public interface FavoritesService {

    /**
     * Retrieves all favorites.
     *
     * @return A list of all favorites.
     */
    List<Favorites> findAll();

    /**
     * Finds favorites by user email.
     *
     * @param email The email of the user.
     * @return Favorites associated with the user.
     */
    Favorites findByUserEmail(final String email);

    /**
     * Creates a new favorite for a user with a given post ID.
     *
     * @param email  The email of the user.
     * @param postId The ID of the post to be added as a favorite.
     * @return The created Favorites object.
     */
    Favorites create(final String email, final String postId);

    /**
     * Removes a post from the favorites of a user.
     *
     * @param email  The email of the user.
     * @param postId The ID of the post to be removed.
     * @return The updated Favorites object after removing the post.
     */
    Favorites removePost(final String email, final String postId);

    /**
     * Deletes all favorites associated with a user.
     *
     * @param email The email of the user.
     */
    void delete(final String email);
}
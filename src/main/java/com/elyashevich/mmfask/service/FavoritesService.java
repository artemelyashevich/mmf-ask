package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Favorites;

import java.util.List;

public interface FavoritesService {

    List<Favorites> findAll();

    Favorites findByUserId(final String userId);

    Favorites create(final String userId, final String postId);

    Favorites addPost(final String userId, final String postId);

    Favorites removePost(final String userId, final String postId);

    void delete(final String userId);
}

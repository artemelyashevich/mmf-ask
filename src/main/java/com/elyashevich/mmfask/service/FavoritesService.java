package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Favorites;

import java.util.List;

public interface FavoritesService {

    List<Favorites> findAll();

    Favorites findByUserEmail(final String email);

    Favorites create(final String email, final String postId);

    Favorites removePost(final String email, final String postId);

    void delete(final String email);
}

package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.FavoritesController;
import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import com.elyashevich.mmfask.api.mapper.FavoritesMapper;
import com.elyashevich.mmfask.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class FavoritesControllerImpl implements FavoritesController {

    private final FavoritesService favoritesService;
    private final FavoritesMapper favoritesMapper;

    @Override
    public List<FavoritesDto> findAll() {
        var favorites = this.favoritesService.findAll();
        return this.favoritesMapper.toDto(favorites);
    }

    @Override
    public FavoritesDto findByUserEmail(final @PathVariable("userEmail") String email) {
        var favorites = this.favoritesService.findByUserEmail(email);
        return this.favoritesMapper.toDto(favorites);
    }

    @Override
    public FavoritesDto addPost(
            final @PathVariable("postId") String postId,
            final @RequestParam("email") String email
    ) {
        var favorites = this.favoritesService.create(email, postId);
        return this.favoritesMapper.toDto(favorites);
    }

    @Override
    public FavoritesDto removePost(
            final @PathVariable("postId") String postId,
            final @RequestParam("email") String email
    ) {
        var favorites = this.favoritesService.removePost(email, postId);
        return this.favoritesMapper.toDto(favorites);
    }

    @Override
    public void delete(final @RequestParam("email") String email) {
        this.favoritesService.delete(email);
    }
}
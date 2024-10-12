package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import com.elyashevich.mmfask.api.mapper.FavoritesMapper;
import com.elyashevich.mmfask.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final FavoritesMapper favoritesMapper;

    @GetMapping
    public List<FavoritesDto> findAll() {
        var favorites = this.favoritesService.findAll();
        return this.favoritesMapper.toDto(favorites);
    }

    @GetMapping("/user/{userEmail}")
    public FavoritesDto findByUserEmail(final @PathVariable("userEmail") String email){
        var favorites = this.favoritesService.findByUserEmail(email);
        return this.favoritesMapper.toDto(favorites);
    }

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritesDto addPost(
            final @PathVariable("postId") String postId,
            final Principal principal
    ) {
        var favorites = this.favoritesService.create(principal.getName(), postId);
        return this.favoritesMapper.toDto(favorites);
    }

    @PutMapping("/{postId}")
    public FavoritesDto removePost(
            final @PathVariable("postId") String postId,
            final Principal principal
    ){
        var favorites = this.favoritesService.removePost(principal.getName(), postId);
        return this.favoritesMapper.toDto(favorites);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final Principal principal) {
        this.favoritesService.delete(principal.getName());
    }
}

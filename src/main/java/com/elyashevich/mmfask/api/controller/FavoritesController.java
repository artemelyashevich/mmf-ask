package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import com.elyashevich.mmfask.api.mapper.FavoritesMapper;
import com.elyashevich.mmfask.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public FavoritesDto findByUserEmail(final @PathVariable("userEmail") String email) {
        var favorites = this.favoritesService.findByUserEmail(email);
        return this.favoritesMapper.toDto(favorites);
    }

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#email == authentication.principal")
    public FavoritesDto addPost(
            final @PathVariable("postId") String postId,
            final @RequestParam("email") String email
    ) {
        var favorites = this.favoritesService.create(email, postId);
        return this.favoritesMapper.toDto(favorites);
    }

    @PutMapping("/{postId}")
    @PreAuthorize("#email == authentication.principal")
    public FavoritesDto removePost(
            final @PathVariable("postId") String postId,
            final @RequestParam("email") String email
    ) {
        var favorites = this.favoritesService.removePost(email, postId);
        return this.favoritesMapper.toDto(favorites);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#email == authentication.principal")
    public void delete(final @RequestParam("email") String email) {
        this.favoritesService.delete(email);
    }
}

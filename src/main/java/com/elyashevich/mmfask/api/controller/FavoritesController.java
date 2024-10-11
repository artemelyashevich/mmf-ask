package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.entity.Favorites;
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

    @GetMapping
    public List<Favorites> findAll() {
        return this.favoritesService.findAll();
    }

    @GetMapping("{userEmail}")
    public Favorites findByUserEmail(final @PathVariable("userEmail") String email){
        return this.favoritesService.findByUserEmail(email);
    }

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Favorites addPost(
            final @PathVariable("postId") String postId,
            final Principal principal
    ) {
        return this.favoritesService.create(principal.getName(), postId);
    }

    @PutMapping("/{postId}")
    public Favorites removePost(
            final @PathVariable("postId") String postId,
            final Principal principal
    ){
        return this.favoritesService.removePost(principal.getName(), postId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final Principal principal) {
        this.favoritesService.delete(principal.getName());
    }
}

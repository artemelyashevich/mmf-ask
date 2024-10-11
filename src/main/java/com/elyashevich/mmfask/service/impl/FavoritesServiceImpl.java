package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Favorites;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.FavoritesRepository;
import com.elyashevich.mmfask.service.FavoritesService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserService userService;
    private final PostService postService;

    @Override
    public List<Favorites> findAll() {
        return this.favoritesRepository.findAll();
    }

    @Transactional
    @Override
    public Favorites findByUserEmail(final String email) {
        var user = this.userService.findByEmail(email);
        return this.favoritesRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with this ID = %s has no favorites.".formatted(email))
                );
    }

    @Transactional
    @Override
    public Favorites create(final String email, final String postId) {
        var user = this.userService.findByEmail(email);
        Favorites favorites = null;
        var post = this.postService.findById(postId);
        if (this.favoritesRepository.existsByUser(user)) {
            favorites = this.findByUserEmail(email);
            var posts = favorites.getPosts();
            this.checkIfFavoritesContainsPost(favorites, post);
            posts.add(post);
            favorites.setPosts(posts);
        } else {
            favorites = Favorites.builder()
                    .user(this.userService.findByEmail(email))
                    .posts(
                            List.of(
                                    post
                            )
                    )
                    .build();
        }
        return this.favoritesRepository.save(favorites);
    }

    @Transactional
    @Override
    public Favorites removePost(final String email, final String postId) {
        var favorites = this.findByUserEmail(email);
        var posts = favorites.getPosts();
        var post = this.postService.findById(postId);
        favorites.setPosts(
                posts.stream()
                        .filter(p -> !Objects.equals(p, post))
                        .toList()
        );
        return this.favoritesRepository.save(favorites);
    }

    @Transactional
    @Override
    public void delete(final String email) {
        var favorites = this.findByUserEmail(email);
        this.favoritesRepository.delete(favorites);
    }

    private void checkIfFavoritesContainsPost(final Favorites favorites, final Post post) {
        if (favorites.getPosts().contains(post)) {
            throw new ResourceAlreadyExistsException(
                    "Post with such id = %s already exists in favorites.".formatted(post)
            );
        }
    }
}

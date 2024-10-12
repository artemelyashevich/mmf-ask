package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Favorites;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.FavoritesRepository;
import com.elyashevich.mmfask.service.FavoritesService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
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
        log.debug("Attempting to find all favorites by user email '{}'.", email);

        var user = this.userService.findByEmail(email);
        var favorites = this.favoritesRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with this ID = %s has no favorites.".formatted(email))
                );

        log.info("Favorites by user email '{}' has been found.", email);
        return favorites;
    }

    @Transactional
    @Override
    public Favorites create(final String email, final String postId) {
        log.debug("Attempting to add new favorite with postId '{}' and user email {}.", postId, email);

        var user = userService.findByEmail(email);
        var favorites = favoritesRepository.existsByUser(user) ?
                updateFavorites(user, postId) : createNewFavorites(user, postId);

        log.debug("New favorite added with postId '{}' and user email {}.", postId, email);
        return favorites;
    }

    @Transactional
    @Override
    public Favorites removePost(final String email, final String postId) {
        log.debug("Attempting to remove post from favorites by user email '{}' and postId '{}'.", email, postId);

        var favorites = this.findByUserEmail(email);
        var posts = favorites.getPosts();
        var post = this.postService.findById(postId);
        favorites.setPosts(
                posts.stream()
                        .filter(p -> !Objects.equals(p, post))
                        .toList()
        );

        log.info("Post from favorites by user email '{}' and postId '{}' has been removed.", email, postId);
        return this.favoritesRepository.save(favorites);
    }

    @Transactional
    @Override
    public void delete(final String email) {
        log.debug("Attempting to delete favorites by user email '{}'.", email);

        var favorites = this.findByUserEmail(email);
        this.favoritesRepository.delete(favorites);

        log.info("Favorite by user email '{}' has been deleted.", email);
    }

    private void checkIfFavoritesContainsPost(final Favorites favorites, final Post post) {
        if (favorites.getPosts().contains(post)) {
            throw new ResourceAlreadyExistsException(
                    "Post with such id = %s already exists in favorites.".formatted(post)
            );
        }
    }

    private Favorites updateFavorites(final User user, final String postId) {
        var favorites = findByUserEmail(user.getEmail());
        var post = postService.findById(postId);
        checkIfFavoritesContainsPost(favorites, post);
        favorites.getPosts().add(post);
        return favoritesRepository.save(favorites);
    }

    private Favorites createNewFavorites(final User user, final String postId) {
        var post = postService.findById(postId);
        var favorites = Favorites.builder()
                .user(user)
                .posts(Collections.singletonList(post))
                .build();
        return favoritesRepository.save(favorites);
    }
}

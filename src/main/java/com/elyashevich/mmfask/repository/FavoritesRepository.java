package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Favorites;
import com.elyashevich.mmfask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FavoritesRepository extends MongoRepository<Favorites, String> {

    Optional<Favorites> findByUser(final User user);

    Boolean existsByUser(final User user);
}

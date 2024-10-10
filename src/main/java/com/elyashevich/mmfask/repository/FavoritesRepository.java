package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Favorites;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FavoritesRepository extends MongoRepository<Favorites, String> {

    Optional<Favorites> findByUserId(final String id);

    boolean existsByUserId(final String id);
}

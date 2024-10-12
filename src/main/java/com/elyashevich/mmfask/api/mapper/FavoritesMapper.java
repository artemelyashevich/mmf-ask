package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import com.elyashevich.mmfask.entity.Favorites;

import java.util.List;

/**
 * Mapper interface for mapping between Favorites and FavoritesDto objects.
 */
public interface FavoritesMapper {

    /**
     * Converts a Favorites object to a FavoritesDto object.
     *
     * @param favorites The Favorites object to convert.
     * @return The corresponding FavoritesDto object.
     */
    FavoritesDto toDto(final Favorites favorites);

    /**
     * Converts a list of Favorites objects to a list of FavoritesDto objects.
     *
     * @param favorites The list of Favorites objects to convert.
     * @return A list of corresponding FavoritesDto objects.
     */
    List<FavoritesDto> toDto(final List<Favorites> favorites);
}

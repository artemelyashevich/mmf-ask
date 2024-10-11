package com.elyashevich.mmfask.api.mapper;

import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import com.elyashevich.mmfask.entity.Favorites;

import java.util.List;

public interface FavoritesMapper {

    FavoritesDto toDto(final Favorites favorites);

    List<FavoritesDto> toDto(final List<Favorites> favorites);
}

package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.favorites.FavoritesDto;
import com.elyashevich.mmfask.api.mapper.FavoritesMapper;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.entity.Favorites;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FavoritesMapperImpl implements FavoritesMapper {

    private final PostMapper postMapper;

    @Override
    public FavoritesDto toDto(Favorites favorites) {
        return new FavoritesDto(
                favorites.getId(),
                favorites.getUser().getId(),
                this.postMapper.toDto(favorites.getPosts()),
                favorites.getCreatedAt(),
                favorites.getUpdatedAt()
        );
    }

    @Override
    public List<FavoritesDto> toDto(List<Favorites> favorites) {
        return favorites.stream()
                .map(this::toDto)
                .toList();
    }
}

package com.elyashevich.mmfask.api.mapper;

import java.util.List;

public interface Mappable<E, D> {

    D toDto(final E entity);

    List<D> toDto(final List<E> entities);

    E toEntity(final D dto);
}

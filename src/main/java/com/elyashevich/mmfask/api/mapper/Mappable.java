package com.elyashevich.mmfask.api.mapper;

import java.util.List;

public interface Mappable <E, D>{

    D toDto(E entity);

    List<D> toDto(List<E> entities);

    E toEntity(D dto);
}

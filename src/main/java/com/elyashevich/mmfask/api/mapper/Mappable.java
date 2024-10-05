package com.elyashevich.mmfask.api.mapper;

import java.util.List;

/**
 * Interface for mapping between entity and DTO objects.
 *
 * @param <E> the entity type
 * @param <D> the DTO type
 */
public interface Mappable<E, D> {

    /**
     * Converts an entity to a DTO object.
     *
     * @param entity the entity to convert
     * @return the corresponding DTO object
     */
    D toDto(final E entity);

    /**
     * Converts a list of entities to a list of DTO objects.
     *
     * @param entities the list of entities to convert
     * @return a list of corresponding DTO objects
     */
    List<D> toDto(final List<E> entities);

    /**
     * Converts a DTO object to an entity.
     *
     * @param dto the DTO object to convert
     * @return the corresponding entity
     */
    E toEntity(final D dto);
}
package com.elyashevich.mmfask.service.contract;

public interface CrudService<E> {

    E findById(final String id);

    E create(final E entity);

    E update(final String id, final E entity);

    void delete(final String id);
}

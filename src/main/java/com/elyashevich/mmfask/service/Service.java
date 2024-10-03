package com.elyashevich.mmfask.service;

import java.util.List;

public interface Service <Entity>{
    List<Entity> findAll();

    Entity findByName(final String name);

    Entity findById(final String id);

    Entity create(final Entity dto);

    Entity update(final String id, Entity dto);

    void delete(final String id);
}

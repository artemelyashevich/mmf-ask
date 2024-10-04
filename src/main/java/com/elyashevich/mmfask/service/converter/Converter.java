package com.elyashevich.mmfask.service.converter;

public interface Converter<Entity> {

    Entity update(Entity oldEntity, Entity newEntity);

}

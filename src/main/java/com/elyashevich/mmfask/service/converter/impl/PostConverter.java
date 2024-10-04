package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.service.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostConverter implements Converter<Post> {

    @Override
    public Post update(Post oldEntity, Post newEntity) {
        oldEntity.setTitle(newEntity.getTitle());
        oldEntity.setDescription(newEntity.getDescription());
        oldEntity.setProgrammingLanguage(newEntity.getProgrammingLanguage());
        oldEntity.setCategories(newEntity.getCategories());
        return oldEntity;
    }
}

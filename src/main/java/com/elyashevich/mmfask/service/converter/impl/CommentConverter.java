package com.elyashevich.mmfask.service.converter.impl;

import com.elyashevich.mmfask.entity.Comment;
import com.elyashevich.mmfask.service.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter implements Converter<Comment> {

    @Override
    public Comment update(Comment oldEntity, Comment newEntity) {
        return null;
    }
}

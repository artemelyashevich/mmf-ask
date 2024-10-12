package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Comment;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByUser(final User user);

    List<Comment> findAllByPost(final Post post);

    List<Comment> findAllByUserAndPost(final User user, final Post post);
}

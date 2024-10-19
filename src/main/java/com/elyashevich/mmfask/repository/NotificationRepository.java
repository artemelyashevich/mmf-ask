package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Notification;
import com.elyashevich.mmfask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByUser(final User user);
}

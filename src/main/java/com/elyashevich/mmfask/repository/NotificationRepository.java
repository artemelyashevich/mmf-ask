package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}

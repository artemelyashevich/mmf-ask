package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BadgeRepository extends MongoRepository<Badge, String> {
    List<Badge> findByTriggerType(BadgeTriggerType triggerType);
}

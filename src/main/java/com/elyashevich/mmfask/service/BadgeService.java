package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.entity.BadgeTriggerType;

import java.util.List;

public interface BadgeService {

    List<Badge> findAll();

    Badge create(Badge badge);

    Badge update(String id, Badge badge);
}

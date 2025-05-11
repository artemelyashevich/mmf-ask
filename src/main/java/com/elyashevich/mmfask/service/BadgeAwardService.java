package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.BadgeTriggerType;

public interface BadgeAwardService {

    void processAction(String userId, BadgeTriggerType triggerType);
}

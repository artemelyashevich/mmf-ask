package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.BadgeRepository;
import com.elyashevich.mmfask.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;

    @Override
    public List<Badge> findAll() {
        log.debug("Attempting find all badges");

        var badges = this.badgeRepository.findAll();

        log.info("Badges found: {}", badges.size());
        return badges;
    }

    @Override
    public Badge create(Badge badge) {
        log.debug("Attempting create new badge");

        var newBadge = this.badgeRepository.save(badge);

        log.info("New badge created: {}", newBadge);
        return newBadge;
    }

    @Override
    @Transactional
    public Badge update(String id, Badge badge) {
        log.debug("Attempting update badge by id: {}", id);

        var oldBadge = this.badgeRepository.findById(id).orElseThrow(
                () -> {
                    var message = "Badge with id: '%s' was not found".formatted(id);
                    log.warn(message);
                    return new ResourceNotFoundException(message);
                }
        );

        oldBadge.setConditionValue(badge.getConditionValue());
        oldBadge.setDescription(badge.getDescription());
        oldBadge.setName(badge.getName());
        oldBadge.setTriggerType(badge.getTriggerType());
        oldBadge.setIconUrl(badge.getIconUrl());

        var updated = this.badgeRepository.save(oldBadge);

        log.info("Badge updated: {}", updated);
        return updated;
    }
}

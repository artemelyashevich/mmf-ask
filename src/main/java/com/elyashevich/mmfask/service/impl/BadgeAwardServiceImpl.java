package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import com.elyashevich.mmfask.repository.BadgeRepository;
import com.elyashevich.mmfask.service.BadgeAwardService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeAwardServiceImpl implements BadgeAwardService {

    private final UserService userService;
    private final BadgeRepository badgeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void processAction(String email, BadgeTriggerType triggerType) {

        var user = this.userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        user.getStats().merge(triggerType.name(), 1, Integer::sum);

        var badgesToChek = this.badgeRepository.findByTriggerType(triggerType);

        var currentCount = user.getStats().getOrDefault(triggerType.name(), 0);

        badgesToChek.forEach(badge -> {
            if (currentCount >= badge.getConditionValue() && !user.getBadges().contains(badge)) {
                user.getBadges().add(badge);
                this.userService.updateBadges(user);
                sendBadgeNotification(email, badge);
            }
        });
    }

    private void sendBadgeNotification(String userId, Badge badge) {
        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/badges",
                badge
        );
    }
}

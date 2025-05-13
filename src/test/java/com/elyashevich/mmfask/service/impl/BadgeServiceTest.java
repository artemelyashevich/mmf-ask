package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.BadgeRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BadgeServiceTest extends BaseIntegrationTest {

    @Autowired
    private BadgeServiceImpl badgeService;

    @Autowired
    private BadgeRepository badgeRepository;

    @BeforeEach
    void setUp() {
        badgeRepository.deleteAll();
    }


    @Test
    void findAll_ReturnsAllBadges() {
        // Arrange
        var badge1 = Badge.builder()
                .name("Badge 1")
                .description("Description 1")
                .conditionValue(1)
                .triggerType(BadgeTriggerType.ANSWER_POSTED)
                .iconUrl("icon1.png")
                .build();
        var badge2 = Badge.builder()
                .name("Badge 2")
                .description("Description 2")
                .conditionValue(2)
                .triggerType(BadgeTriggerType.ANSWER_POSTED)
                .iconUrl("icon2.png")
                .build();
        badgeRepository.saveAll(List.of(badge1, badge2));

        // Act
        var badges = badgeService.findAll();

        // Assert
        assertAll(
                () -> assertNotNull(badges),
                () -> assertEquals(2, badges.size()),
                () -> assertTrue(badges.stream().anyMatch(b -> b.getName().equals("Badge 1"))),
                () -> assertTrue(badges.stream().anyMatch(b -> b.getName().equals("Badge 2")))
        );
    }

    @Test
    void create_ValidBadge_CreatesBadge() {
        // Arrange
        var badge = Badge.builder()
                .name("New Badge")
                .description("New Description")
                .conditionValue(3)
                .triggerType(BadgeTriggerType.ANSWER_POSTED)
                .iconUrl("new_icon.png")
                .build();

        // Act
        var savedBadge = badgeService.create(badge);

        // Assert
        assertAll(
                () -> assertNotNull(savedBadge.getId()),
                () -> assertEquals("New Badge", savedBadge.getName()),
                () -> assertEquals("New Description", savedBadge.getDescription()),
                () -> assertEquals(3, savedBadge.getConditionValue()),
                () -> assertEquals(BadgeTriggerType.ANSWER_POSTED, savedBadge.getTriggerType()),
                () -> assertEquals("new_icon.png", savedBadge.getIconUrl())
        );
    }

    @Test
    void update_ExistingBadge_UpdatesBadge() {
        // Arrange
        var badge = Badge.builder()
                .name("Old Badge")
                .description("Old Description")
                .conditionValue(1)
                .triggerType(BadgeTriggerType.ANSWER_POSTED)
                .iconUrl("old_icon.png")
                .build();
        var savedBadge = badgeRepository.save(badge);

        var updatedBadge = Badge.builder()
                .name("Updated Badge")
                .description("Updated Description")
                .conditionValue(5)
                .triggerType(BadgeTriggerType.ANSWER_POSTED)
                .iconUrl("updated_icon.png")
                .build();

        // Act
        var result = badgeService.update(savedBadge.getId(), updatedBadge);

        // Assert
        assertAll(
                () -> assertEquals(savedBadge.getId(), result.getId()),
                () -> assertEquals("Updated Badge", result.getName()),
                () -> assertEquals("Updated Description", result.getDescription()),
                () -> assertEquals(5, result.getConditionValue()),
                () -> assertEquals(BadgeTriggerType.ANSWER_POSTED, result.getTriggerType()),
                () -> assertEquals("updated_icon.png", result.getIconUrl())
        );
    }

    @Test
    void update_NonExistingBadge_ThrowsResourceNotFoundException() {
        // Arrange
        var updatedBadge = Badge.builder()
                .name("Updated Badge")
                .description("Updated Description")
                .conditionValue(5)
                .triggerType(BadgeTriggerType.ANSWER_POSTED)
                .iconUrl("updated_icon.png")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> badgeService.update("nonexistent-id", updatedBadge));
    }
}
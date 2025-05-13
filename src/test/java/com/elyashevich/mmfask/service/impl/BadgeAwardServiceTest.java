package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Badge;
import com.elyashevich.mmfask.entity.BadgeTriggerType;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.repository.BadgeRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BadgeAwardService;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class BadgeAwardServiceTest extends BaseIntegrationTest {

    @Autowired
    private BadgeAwardService badgeAwardService;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        badgeRepository.deleteAll();

        // Create a user with email 'test@example.com'
        var user = User.builder()
                .email("test@example.com")
                .stats(Map.of())
                .badges(List.of())
                .build();
        userRepository.save(user);
    }

    @ParameterizedTest
    @WithMockUser(username = "test@example.com")
    @MethodSource("provideScenariosForBadgeAward")
    void processAction_ParameterizedTest(final BadgeAwardTestData testData) {
        // Arrange
        var badge = Badge.builder()
                .id("badge1")
                .name("First Post Badge")
                .triggerType(testData.triggerType())
                .conditionValue(testData.badgeConditionValue())
                .build();
        badgeRepository.save(badge);

        var user = User.builder()
                .email(testData.email())
                .stats(Map.of(testData.triggerType().name(), testData.initialTriggerCount()))
                .badges(testData.userAlreadyHasBadge() ? List.of(badge) : List.of())
                .build();
        userRepository.save(user);

        // Act
        badgeAwardService.processAction(testData.email(), testData.triggerType());

        // Assert
        var updatedUser = userRepository.findByEmail(testData.email()).orElseThrow();
        assertAll(
                () -> assertEquals(
                        testData.initialTriggerCount(),
                        updatedUser.getStats().get(testData.triggerType().name())
                ),
                () -> assertEquals(
                        testData.badgeShouldBeAwarded(),
                        updatedUser.getBadges().contains(badge)
                )
        );
    }

    static Stream<Arguments> provideScenariosForBadgeAward() {
        return Stream.of(
                Arguments.of(new BadgeAwardTestData("test1@example.com", BadgeTriggerType.ANSWER_POSTED, 0, 1, false, false)),
                Arguments.of(new BadgeAwardTestData("test2@example.com", BadgeTriggerType.ANSWER_POSTED, 0, 5, false, false)),
                Arguments.of(new BadgeAwardTestData("test3@example.com", BadgeTriggerType.ANSWER_POSTED, 1, 1, true, true))
        );
    }

    record BadgeAwardTestData(
            String email,
            BadgeTriggerType triggerType,
            int initialTriggerCount,
            int badgeConditionValue,
            boolean userAlreadyHasBadge,
            boolean badgeShouldBeAwarded
    ) {
    }
}
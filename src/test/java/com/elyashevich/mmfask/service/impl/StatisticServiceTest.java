package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.repository.CategoryRepository;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class StatisticServiceTest extends BaseIntegrationTest {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        categoryRepository.deleteAll();
        programmingLanguageRepository.deleteAll();
    }

    @Test
    void userStatistic_ReturnsCorrectStatistics() {
        // Arrange
        var user1 = buildUser("user1", "User One");
        var user2 = buildUser("user2", "User Two");
        userRepository.saveAll(List.of(user1, user2));

        // Act
        var result = statisticService.userStatistic();

        // Assert
        assertAll(
                () -> assertEquals(2, result.countOfUsers(), "Count of users should be 2"),
                () -> assertEquals(2, result.users().size(), "The DTO should contain 2 users")
        );
    }

    @Test
    void postStatistic_ReturnsCorrectStatistics() {
        // Arrange
        var creator = buildUser("user1", "User One");
        userRepository.save(creator);

        var post1 = buildPost("post1", creator, 100, 10, 2);
        var post2 = buildPost("post2", creator,200, 20, 5);


        postRepository.saveAll(List.of(post1, post2));

        // Act
        var result = statisticService.postStatistic();

        // Assert
        assertAll(
                () -> assertEquals(2, result.countOfPosts(), "Count of posts should be 2"),
                () -> assertEquals(18.5, result.averageRating(), "Average rating should be 18.5"),
                () -> assertEquals(2, result.posts().size(), "The DTO should contain 2 posts")
        );
    }

    @Test
    void categoryStatistics_ReturnsCorrectStatistics() {
        // Arrange
        var category1 = buildCategory("category1", "Technology");
        category1.setPosts(List.of(buildPost("post1", 100, 10, 2)));
        categoryRepository.save(category1);

        // Act
        var result = statisticService.categoryStatistics();

        // Assert
        assertAll(
                () -> assertEquals(1, result.countOfCategories(), "Count of categories should be 2"),
                () -> assertEquals(1, result.categories().size(), "The DTO should contain 2 categories"),
                () -> assertEquals(1, result.mostPopular().size(), "Most popular categories should contain 2 entries"),
                () -> assertEquals(1L, result.mostPopular().get("Technology"), "Technology category should have 2 posts")
        );
    }

    // Helper Methods
    private User buildUser(String id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .build();
    }

    private Post buildPost(String id, long views, long likes, long dislikes) {
        var programmingLanguage = buildProgrammingLanguage("1", "Java");
        programmingLanguageRepository.save(programmingLanguage);

        return Post.builder()
                .id(id)
                .views(views)
                .likes(likes)
                .programmingLanguage(programmingLanguage)
                .dislikes(dislikes)
                .categories(Set.of())
                .attachmentImages(Set.of())
                .build();
    }

    private Post buildPost(String id, User creator, long views, long likes, long dislikes) {
        var programmingLanguage = buildProgrammingLanguage("2", "Python");
        programmingLanguageRepository.save(programmingLanguage);

        return Post.builder()
                .id(id)
                .creator(creator)
                .views(views)
                .likes(likes)
                .programmingLanguage(programmingLanguage)
                .dislikes(dislikes)
                .categories(Set.of())
                .attachmentImages(Set.of())
                .build();
    }

    private Category buildCategory(String id, String name) {
        return Category.builder()
                .id(id)
                .name(name)
                .posts(List.of())
                .build();
    }

    private ProgrammingLanguage buildProgrammingLanguage(String id, String name) {
        return ProgrammingLanguage.builder()
                .id(id)
                .name(name)
                .build();
    }
}
package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.entity.Resume;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.CategoryRepository;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.repository.ResumeRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.CategoryService;
import com.elyashevich.mmfask.service.PostService;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import com.elyashevich.mmfask.service.ResumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class ResumeServiceTest extends BaseIntegrationTest {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProgrammingLanguageService programmingLanguageService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        resumeRepository.deleteAll();
        programmingLanguageRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        setupMockUser();
    }


    private void setupMockUser() {
        userRepository.save(
                User.builder()
                        .email("test@example.com")
                        .password("mock-password")
                        .build()
        );
    }

    @Test
    void findById_ExistingResume_ReturnsResume() {
        // Arrange
        var resume = buildResume("1", 5);
        resumeRepository.save(resume);

        // Act
        var result = resumeService.findById(resume.getId());

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(resume.getId(), result.getId()),
                () -> assertEquals(resume.getExperienceInYears(), result.getExperienceInYears())
        );
    }

    @Test
    void findById_NonExistingResume_ThrowsResourceNotFoundException() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> resumeService.findById("nonexistent-id"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void create_ValidResume_CreatesResume() {
        // Arrange
        var category = buildCategory("1", "Technology");
        var programmingLanguage = buildProgrammingLanguage("1", "Java");

        // Save the necessary entities to the database
        categoryRepository.save(category);
        programmingLanguageRepository.save(programmingLanguage);

        // Save posts for the authenticated user (test@example.com)
        var post1 = Post.builder()
                .id("post1")
                .views(100L)
                .categories(Set.of())
                .programmingLanguage(programmingLanguage)
                .build();
        var post2 = Post.builder()
                .id("post2")
                .views(200L)
                .categories(Set.of())
                .programmingLanguage(programmingLanguage)
                .build();
        postService.create(post1);
        postService.create(post2);

        // Create a new resume
        var resume = buildResume(null, 5);
        resume.setCategories(Set.of(category));
        resume.setProgrammingLanguages(Set.of(programmingLanguage));

        // Act
        var result = resumeService.create(resume);

        // Assert
        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertEquals(5, result.getExperienceInYears()),
                () -> assertEquals(0, result.getLevel()),
                () -> assertEquals(1, result.getCategories().size()),
                () -> assertEquals(1, result.getProgrammingLanguages().size())
        );
    }

    @Test
    void update_ExistingResume_UpdatesResume() {
        // Arrange
        var oldResume = buildResume("1", 5);
        resumeRepository.save(oldResume);

        // Prepopulate the database with the required Category and ProgrammingLanguage
        var updatedCategory = buildCategory("2", "Science");
        var updatedProgrammingLanguage = buildProgrammingLanguage("2", "Python");
        categoryService.create(updatedCategory);
        programmingLanguageService.create(updatedProgrammingLanguage);

        // Prepare the updated Resume
        var updatedResume = buildResume(null, 10);
        updatedResume.setCategories(Set.of(updatedCategory));
        updatedResume.setProgrammingLanguages(Set.of(updatedProgrammingLanguage));

        // Act
        var result = resumeService.update(oldResume.getId(), updatedResume);

        // Assert
        assertAll(
                () -> assertEquals(oldResume.getId(), result.getId()), // ID should remain unchanged
                () -> assertEquals(10, result.getExperienceInYears()), // Experience should be updated
                () -> assertEquals(1, result.getCategories().size()), // Categories should be updated
                () -> assertEquals(1, result.getProgrammingLanguages().size()) // Programming languages should be updated
        );
    }

    @Test
    void update_NonExistingResume_ThrowsResourceNotFoundException() {
        // Arrange
        var updatedResume = buildResume(null, 10);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> resumeService.update("nonexistent-id", updatedResume));
    }

    @SuppressWarnings("all")
    @Test
    void delete_ExistingResume_RemovesResume() {
        // Arrange
        var resume = buildResume("1", 5);
        resumeRepository.save(resume);

        // Act
        resumeService.delete(resume.getId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> resumeService.findById(resume.getId()));
    }

    @Test
    void delete_NonExistingResume_ThrowsResourceNotFoundException() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> resumeService.delete("nonexistent-id"));
    }

    @Test
    void findByUserId_ExistingUserId_ReturnsResume() {
        // Arrange
        var userId = "user-1";
        var resume = buildResume("1", 5);
        resume.setUserId(userId);
        resumeRepository.save(resume);

        // Act
        var result = resumeService.findByUserId(userId);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userId, result.getUserId())
        );
    }

    @Test
    void findByUserId_NonExistingUserId_ThrowsResourceNotFoundException() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> resumeService.findByUserId("nonexistent-user-id"));
    }

    // Helper Methods
    private Resume buildResume(String id, int experienceInYears) {
        return Resume.builder()
                .id(id)
                .experienceInYears(experienceInYears)
                .categories(Set.of())
                .programmingLanguages(Set.of())
                .build();
    }

    private Category buildCategory(String id, String name) {
        return Category.builder()
                .id(id)
                .name(name)
                .build();
    }

    private ProgrammingLanguage buildProgrammingLanguage(String id, String name) {
        return ProgrammingLanguage.builder()
                .id(id)
                .name(name)
                .build();
    }
}
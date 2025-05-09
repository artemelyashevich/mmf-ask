package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.category.CategoryStatisticsDto;
import com.elyashevich.mmfask.api.dto.post.PostStatisticsDto;
import com.elyashevich.mmfask.api.dto.user.UserStatisticsDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.entity.Category;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.repository.CategoryRepository;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    private final PostMapper postMapper;

    private final UserMapper userMapper;

    private final CategoryMapper categoryMapper;

    @Override
    public UserStatisticsDto userStatistic() {
        var users = this.userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        long count = users.size();
        return new UserStatisticsDto(count, this.userMapper.toDto(users));
    }

    @Override
    public PostStatisticsDto postStatistic() {
        var posts = this.postRepository.findAll(Sort.by(Sort.Direction.DESC, "views"));
        long countOfPosts = posts.size();
        double averageRating = (double) (
                posts.stream().mapToLong(Post::getLikes).sum() + posts.stream().mapToLong(Post::getDislikes).sum()
        ) / 2;
        return new PostStatisticsDto(countOfPosts, averageRating, this.postMapper.toDto(posts));
    }

    @Override
    public CategoryStatisticsDto categoryStatistics() {
        var categories = this.categoryRepository.findAll();
        var countOfCategories = (long) categories.size();
        var mostPopular = categories.stream()
                .sorted(Comparator.comparingInt(category -> Optional.of(category.getPosts().size()).orElse(0)))
                .limit(5)
                .collect(Collectors.toMap(Category::getName, category -> (long) category.getPosts().size()));
        return new CategoryStatisticsDto(countOfCategories, this.categoryMapper.toDto(categories), mostPopular);
    }
}

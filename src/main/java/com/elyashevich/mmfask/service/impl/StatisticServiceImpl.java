package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.post.PostStatisticsDto;
import com.elyashevich.mmfask.api.dto.user.UserStatisticsDto;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.repository.PostRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final UserMapper userMapper;

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
}

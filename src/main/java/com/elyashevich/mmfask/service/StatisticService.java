package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.category.CategoryStatisticsDto;
import com.elyashevich.mmfask.api.dto.post.PostStatisticsDto;
import com.elyashevich.mmfask.api.dto.user.UserStatisticsDto;

public interface StatisticService {

    UserStatisticsDto userStatistic();

    PostStatisticsDto postStatistic();

    CategoryStatisticsDto categoryStatistics();
}

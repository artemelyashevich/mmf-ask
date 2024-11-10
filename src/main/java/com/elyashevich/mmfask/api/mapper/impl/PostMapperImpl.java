package com.elyashevich.mmfask.api.mapper.impl;

import com.elyashevich.mmfask.api.dto.post.PostRequestDto;
import com.elyashevich.mmfask.api.dto.post.PostResponseDto;
import com.elyashevich.mmfask.api.mapper.CategoryMapper;
import com.elyashevich.mmfask.api.mapper.PostMapper;
import com.elyashevich.mmfask.api.mapper.ProgrammingLanguageMapper;
import com.elyashevich.mmfask.api.mapper.UserMapper;
import com.elyashevich.mmfask.entity.Post;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final ProgrammingLanguageMapper programmingLanguageMapper;

    @Override
    public PostResponseDto toDto(final Post entity) {
        return new PostResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                this.userMapper.toDto(entity.getCreator()),
                this.programmingLanguageMapper.toDto(entity.getProgrammingLanguage()),
                this.categoryMapper.toDto(entity.getCategories()),
                entity.getViews(),
                entity.getLikes(),
                entity.getDislikes(),
                entity.getAttachmentImages()
                        .stream()
                        .map(ImageUtil::generatePath)
                        .collect(Collectors.toSet()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    @Override
    public List<PostResponseDto> toDto(final List<Post> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Post toEntity(final PostResponseDto dto) {
        return null;
    }

    @Override
    public Post toEntity(final PostRequestDto dto) {
        return Post.builder()
                .title(dto.title())
                .description(dto.description())
                .categories(this.categoryMapper.toEntityFromStrings(dto.namesOfCategories()))
                .creator(User.builder()
                        .email(dto.creatorEmail())
                        .build())
                .programmingLanguage(this.programmingLanguageMapper.toEntityFromString(dto.programmingLanguageName()))
                .views(dto.views())
                .build();
    }
}

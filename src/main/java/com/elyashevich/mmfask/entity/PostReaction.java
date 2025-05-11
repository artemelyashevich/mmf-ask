package com.elyashevich.mmfask.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "post_reactions")
@CompoundIndexes({
        @CompoundIndex(name = "user_post_unique", def = "{'userId': 1, 'postId': 1}", unique = true)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PostReaction {
    @Id
    private String id;

    @Field
    private String email;

    @Field
    @DBRef
    private String postId;

    @Field
    private ReactionType type;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum ReactionType {
        LIKE, DISLIKE
    }
}

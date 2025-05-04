package com.elyashevich.mmfask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Post {

    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    @DBRef
    private User creator;

    @DBRef
    private ProgrammingLanguage programmingLanguage;

    @DBRef
    private Set<Category> categories;

    @DBRef
    private Set<Comment> comments;

    private Long views;

    private Long likes;

    private Long dislikes;

    @DBRef
    private Set<AttachmentImage> attachmentImages;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreatedDate
    private LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", creator=").append(creator);
        sb.append(", programmingLanguage=").append(programmingLanguage);
        sb.append(", categories=").append(categories);
        sb.append(", comments=").append(comments);
        sb.append(", views=").append(views);
        sb.append(", likes=").append(likes);
        sb.append(", dislikes=").append(dislikes);
        sb.append(", attachmentImages=").append(attachmentImages);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append('}');
        return sb.toString();
    }
}

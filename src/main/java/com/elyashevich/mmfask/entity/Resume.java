package com.elyashevich.mmfask.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "resumes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Resume {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    @DBRef
    private Set<ProgrammingLanguage> programmingLanguages = new HashSet<>();

    @DBRef
    private Set<Category> categories = new HashSet<>();

    @DBRef
    private Integer experienceInYears = 0;

    @DBRef
    private Integer level = 0;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resume{");
        sb.append("id='").append(id).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", programmingLanguages=").append(programmingLanguages);
        sb.append(", categories=").append(categories);
        sb.append(", experienceInYears=").append(experienceInYears);
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
    }
}

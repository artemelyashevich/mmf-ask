package com.elyashevich.mmfask.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "badges")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Badge {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Indexed
    private String description;

    private String iconUrl;

    private BadgeTriggerType triggerType;

    private int conditionValue;
}
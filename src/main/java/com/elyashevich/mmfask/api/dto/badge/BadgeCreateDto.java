package com.elyashevich.mmfask.api.dto.badge;

import com.elyashevich.mmfask.entity.BadgeTriggerType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record BadgeCreateDto(

        @NotNull(message = "Name must be not null")
        @Length(min = 2, max = 255, message = "Name must be in {min} and {max}")
        String name,

        @NotNull(message = "Description must be not null")
        String description,

        String iconUri,

        @NotNull(message = "Trigger type must be not null")
        BadgeTriggerType triggerType,

        @NotNull(message = "Condition value must be not null")
        int conditionValue
) {
}

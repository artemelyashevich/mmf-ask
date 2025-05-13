package com.elyashevich.mmfask.api.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageDeleteRequest {
    @NotBlank
    private String messageId;
    
    @NotBlank
    private String userId;
}
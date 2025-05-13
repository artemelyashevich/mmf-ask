package com.elyashevich.mmfask.api.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageEditRequest {
    @NotBlank
    private String messageId;
    
    @NotBlank
    private String newContent;
    
    @NotBlank
    private String userId;
}
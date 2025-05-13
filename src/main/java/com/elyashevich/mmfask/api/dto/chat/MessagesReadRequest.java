package com.elyashevich.mmfask.api.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class MessagesReadRequest {
    @NotBlank
    private String recipientId;
    
    @NotEmpty
    private List<String> messageIds;
}
package com.elyashevich.mmfask.api.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    @NotBlank
    private String content;
    
    @NotBlank
    private String senderId;
    
    @NotBlank
    private String recipientId;
}
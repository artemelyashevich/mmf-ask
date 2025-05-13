package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.api.dto.chat.ChatMessageDto;
import com.elyashevich.mmfask.entity.ChatMessage;

import java.util.List;

public interface ChatService {

    ChatMessage sendPrivateMessage(ChatMessageDto messageDto);

    ChatMessage editMessage(String messageId, String newContent, String userId);

    void deleteMessage(String messageId, String userId);

    List<ChatMessage> getChatHistory(String userId1, String userId2);

    void markMessagesAsRead(String recipientId, List<String> messageIds);
}

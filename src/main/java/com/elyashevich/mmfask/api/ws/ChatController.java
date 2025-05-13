package com.elyashevich.mmfask.api.ws;

import com.elyashevich.mmfask.api.dto.chat.ChatMessageDto;
import com.elyashevich.mmfask.api.dto.chat.MessageDeleteRequest;
import com.elyashevich.mmfask.api.dto.chat.MessageEditRequest;
import com.elyashevich.mmfask.api.dto.chat.MessagesReadRequest;
import com.elyashevich.mmfask.entity.ChatMessage;
import com.elyashevich.mmfask.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatMessage sendMessage(@RequestBody @Valid ChatMessageDto messageDto) {
        return chatService.sendPrivateMessage(messageDto);
    }

    @PutMapping
    public ChatMessage editMessage(@RequestBody @Valid MessageEditRequest request) {
        return chatService.editMessage(
                request.getMessageId(),
                request.getNewContent(),
                request.getUserId()
        );
    }

    @DeleteMapping
    public void deleteMessage(@RequestBody @Valid MessageDeleteRequest request) {
        chatService.deleteMessage(request.getMessageId(), request.getUserId());
    }

    @GetMapping("/history/{userId1}/{userId2}")
    public List<ChatMessage> getChatHistory(
            @PathVariable String userId1,
            @PathVariable String userId2) {
        return chatService.getChatHistory(userId1, userId2);
    }

    @MessageMapping("/chat/send")
    public void handleChatMessage(@Payload ChatMessageDto messageDto) {
        chatService.sendPrivateMessage(messageDto);
    }

    @MessageMapping("/chat/read")
    public void markAsRead(@Payload MessagesReadRequest request) {
        chatService.markMessagesAsRead(request.getRecipientId(), request.getMessageIds());
    }
}
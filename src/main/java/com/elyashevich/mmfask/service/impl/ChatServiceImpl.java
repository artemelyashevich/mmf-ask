package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.chat.ChatMessageDto;
import com.elyashevich.mmfask.entity.ChatMessage;
import com.elyashevich.mmfask.entity.MessageStatus;
import com.elyashevich.mmfask.exception.InvalidTokenException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ChatMessageRepository;
import com.elyashevich.mmfask.service.ChatService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository messageRepository;
    private final UserService userService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    @Transactional
    public ChatMessage sendPrivateMessage(ChatMessageDto messageDto) {
        var sender = userService.findById(messageDto.getSenderId());
        var recipient = userService.findById(messageDto.getRecipientId());

        var message = ChatMessage.builder()
                .content(messageDto.getContent())
                .sender(sender)
                .recipient(recipient)
                .status(MessageStatus.SENT)
                .build();

        var savedMessage = messageRepository.save(message);

        messagingTemplate.convertAndSendToUser(
                recipient.getId(),
                "/chat",
                savedMessage
        );

        messagingTemplate.convertAndSendToUser(
                sender.getId(),
                "/chat",
                savedMessage
        );

        return savedMessage;
    }

    @Override
    @Transactional
    public ChatMessage editMessage(String messageId, String newContent, String userId) {
        var message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(messageId));

        if (!message.getSender().getId().equals(userId)) {
            throw new InvalidTokenException();
        }

        message.setContent(newContent);
        message.setStatus(MessageStatus.EDITED);
        var updatedMessage = messageRepository.save(message);

        notifyParticipants(updatedMessage);
        return updatedMessage;
    }

    @Override
    @Transactional
    public void deleteMessage(String messageId, String userId) {
        var message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(messageId));

        if (!message.getSender().getId().equals(userId)) {
            throw new InvalidTokenException();
        }

        message.setStatus(MessageStatus.DELETED);
        message.setContent("Сообщение удалено");
        messageRepository.save(message);

        notifyParticipants(message);
    }

    @Override
    @Transactional
    public List<ChatMessage> getChatHistory(String userId1, String userId2) {
        var user1 = userService.findById(userId1);
        var user2 = userService.findById(userId2);

        return messageRepository.findAllBySenderAndRecipient(user1, user2);
    }

    @Override
    @Transactional
    public void markMessagesAsRead(String recipientId, List<String> messageIds) {
        var messages = messageRepository.findAllById(messageIds);
        messages.forEach(msg -> {
            if (msg.getRecipient().getId().equals(recipientId)) {
                msg.setStatus(MessageStatus.READ);
            }
        });
        messageRepository.saveAll(messages);
    }

    private void notifyParticipants(ChatMessage message) {
        messagingTemplate.convertAndSendToUser(
                message.getSender().getId(),
                "/chat",
                message
        );

        messagingTemplate.convertAndSendToUser(
                message.getRecipient().getId(),
                "/chat",
                message
        );
    }
}

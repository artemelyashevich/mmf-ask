package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.api.dto.chat.ChatMessageDto;
import com.elyashevich.mmfask.entity.ChatMessage;
import com.elyashevich.mmfask.entity.MessageStatus;
import com.elyashevich.mmfask.entity.User;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ChatMessageRepository;
import com.elyashevich.mmfask.repository.UserRepository;
import com.elyashevich.mmfask.service.BaseIntegrationTest;
import com.elyashevich.mmfask.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class ChatServiceTest extends BaseIntegrationTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(
                User.builder()
                        .id("user1")
                        .email("user1@example.com")
                        .build()
        );
        userRepository.save(
                User.builder()
                        .id("user2")
                        .email("user2@example.com")
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("provideSendMessageScenarios")
    void sendPrivateMessage_ValidInputs_SavesAndSendsMessage(final ChatMessageDto messageDto) {
        // Act
        var savedMessage = chatService.sendPrivateMessage(messageDto);

        // Assert
        assertAll(
                () -> assertNotNull(savedMessage.getId()),
                () -> assertEquals(messageDto.getContent(), savedMessage.getContent()),
                () -> assertEquals(MessageStatus.SENT, savedMessage.getStatus()),
                () -> assertEquals(messageDto.getSenderId(), savedMessage.getSender().getId()),
                () -> assertEquals(messageDto.getRecipientId(), savedMessage.getRecipient().getId())
        );
    }

    @ParameterizedTest
    @MethodSource("provideEditMessageScenarios")
    void editMessage_ValidInputs_UpdatesMessage(final String messageId, final String newContent, final String userId) {
        // Arrange
        var message = ChatMessage.builder()
                .id(messageId)
                .content("Old Content")
                .sender(userRepository.findById(userId).orElseThrow())
                .recipient(userRepository.findById("user2").orElseThrow())
                .status(MessageStatus.SENT)
                .build();
        messageRepository.save(message);

        // Act
        var updatedMessage = chatService.editMessage(messageId, newContent, userId);

        // Assert
        assertAll(
                () -> assertEquals(newContent, updatedMessage.getContent()),
                () -> assertEquals(MessageStatus.EDITED, updatedMessage.getStatus())
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEditMessageScenarios")
    void editMessage_InvalidInputs_ThrowsException(final String messageId, final String newContent, final String userId) {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> chatService.editMessage(messageId, newContent, userId));
    }

    @ParameterizedTest
    @MethodSource("provideDeleteMessageScenarios")
    void deleteMessage_ValidInputs_DeletesMessage(final String messageId, final String userId) {
        // Arrange
        var message = ChatMessage.builder()
                .id(messageId)
                .content("Message to delete")
                .sender(userRepository.findById(userId).orElseThrow())
                .recipient(userRepository.findById("user2").orElseThrow())
                .status(MessageStatus.SENT)
                .build();
        messageRepository.save(message);

        // Act
        chatService.deleteMessage(messageId, userId);

        // Assert
        var deletedMessage = messageRepository.findById(messageId).orElseThrow();
        assertAll(
                () -> assertEquals(MessageStatus.DELETED, deletedMessage.getStatus()),
                () -> assertEquals("Сообщение удалено", deletedMessage.getContent())
        );
    }

    @ParameterizedTest
    @MethodSource("provideChatHistoryScenarios")
    void getChatHistory_ValidInputs_ReturnsMessages(final String userId1, final String userId2) {
        // Arrange
        var sender = userRepository.findById(userId1).orElseThrow();
        var recipient = userRepository.findById(userId2).orElseThrow();
        var message1 = ChatMessage.builder()
                .content("Message 1")
                .sender(sender)
                .recipient(recipient)
                .status(MessageStatus.SENT)
                .build();
        var message2 = ChatMessage.builder()
                .content("Message 2")
                .sender(recipient)
                .recipient(sender)
                .status(MessageStatus.SENT)
                .build();
        messageRepository.saveAll(List.of(message1, message2));

        // Act
        var chatHistory = chatService.getChatHistory(userId1, userId2);

        // Assert
        assertEquals(1, chatHistory.size());
    }

    static Stream<Arguments> provideSendMessageScenarios() {
        return Stream.of(
                Arguments.of(new ChatMessageDto("Hello", "user2", "user1")),
                Arguments.of(new ChatMessageDto("Best regards", "user1", "user2"))
        );
    }

    static Stream<Arguments> provideEditMessageScenarios() {
        return Stream.of(
                Arguments.of("msg1", "Updated Content", "user1"),
                Arguments.of("msg2", "New Message Content", "user1")
        );
    }

    static Stream<Arguments> provideInvalidEditMessageScenarios() {
        return Stream.of(
                Arguments.of("msg1", "Unauthorized Edit", "user2"),
                Arguments.of("nonexistent123", "Edit Nonexistent", "user1")
        );
    }

    static Stream<Arguments> provideDeleteMessageScenarios() {
        return Stream.of(
                Arguments.of("msg1", "user1"),
                Arguments.of("msg2", "user1")
        );
    }

    static Stream<Arguments> provideChatHistoryScenarios() {
        return Stream.of(
                Arguments.of("user1", "user2"),
                Arguments.of("user2", "user1")
        );
    }
}

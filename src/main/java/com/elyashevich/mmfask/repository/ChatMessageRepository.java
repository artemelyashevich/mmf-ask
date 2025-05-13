package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.ChatMessage;
import com.elyashevich.mmfask.entity.MessageStatus;
import com.elyashevich.mmfask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findAllBySenderAndRecipient(User user1, User user2);

    List<ChatMessage> findByRecipientIdAndStatus(String recipientId, MessageStatus status);
}

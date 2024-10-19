package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.Notification;
import com.elyashevich.mmfask.repository.NotificationRepository;
import com.elyashevich.mmfask.service.NotificationService;
import com.elyashevich.mmfask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
   // private final SimpMessagingTemplate template;
    private final UserService userService;

    @Transactional
    @Override
    public void sendMessage(final String message, final String email) {
         var notification = this.notificationRepository.save(
                Notification.builder()
                        .message(message)
                        .user(this.userService.findByEmail(email))
                        .build()
        );
     //    this.template.convertAndSend("notification", notification.getMessage());
    }

    @Transactional
    @Override
    public List<Notification> findAll(final String email) {
        return this.notificationRepository.findAllByUser(this.userService.findByEmail(email));
    }
}

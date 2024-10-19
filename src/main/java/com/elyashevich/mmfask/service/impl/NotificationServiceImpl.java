package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.repository.NotificationRepository;
import com.elyashevich.mmfask.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendMessage(String message) {

    }
}

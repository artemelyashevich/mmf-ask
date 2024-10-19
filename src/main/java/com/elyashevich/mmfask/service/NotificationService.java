package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Notification;

import java.util.List;

public interface NotificationService {

    void sendMessage(final String message, final String email);

    List<Notification> findAll(final String email);
}

package com.elyashevich.mmfask.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/notifications")
    public void processMessage(){}
}

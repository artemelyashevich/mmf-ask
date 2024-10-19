package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public String processMessage(){
        var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return email.toString();
    }
}

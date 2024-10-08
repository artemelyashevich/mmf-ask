package com.elyashevich.mmfask.service;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendMessage(final String receiver, final String activationCode, final String templatePath)
            throws MessagingException;
}

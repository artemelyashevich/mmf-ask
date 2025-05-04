package com.elyashevich.mmfask.service;

import jakarta.mail.MessagingException;

/**
 * Service interface for managing email.
 */
public interface MailService {

    /**
     * Sends a message to the specified receiver with the activation code using the provided template.
     *
     * @param receiver       The email address of the message receiver.
     * @param activationCode The activation code to be included in the message.
     * @param templatePath   The path to the template used for the message.
     * @throws MessagingException If an error occurs while sending the message.
     */
    void sendMessage(final String receiver, final String activationCode, final String templatePath)
            throws MessagingException;
}

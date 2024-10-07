package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @Value("${spring.mail.sender.text}")
    private String senderText;

    @Override
    public void sendMessage(String receiver) throws MessagingException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();
        context.setVariables(Map.of(
                "username", receiver,
                "confirmationUrl", "example.com",
                "activation_code", this.generateActivationToken()
        ));

        helper.setFrom(senderEmail);
        helper.setTo(receiver);
        helper.setSubject(receiver);

        String template = this.templateEngine.process("activate_account", context);

        helper.setText(template, true);

        this.javaMailSender.send(mimeMessage);
    }

    private String generateActivationToken() {
        var characters = "0123456789";
        var tokenBuilder = new StringBuilder();
        var random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));
        }
        return tokenBuilder.toString();
    }
}

package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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
    public void sendMessage(
            final String receiver,
            final String activationCode,
            final String templateName
    ) throws MessagingException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();
        context.setVariables(Map.of(
                "username", receiver,
                "activation_code", activationCode
        ));

        helper.setFrom(senderEmail);
        helper.setTo(receiver);
        helper.setSubject(receiver);

        String template = this.templateEngine.process(templateName, context);

        helper.setText(template, true);

        this.javaMailSender.send(mimeMessage);
    }
}

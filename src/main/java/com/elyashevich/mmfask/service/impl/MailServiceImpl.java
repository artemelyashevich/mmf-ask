package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Setter
    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @Override
    public void sendMessage(
            final String receiver,
            final String activationCode,
            final String templateName
    ) throws MessagingException {
        log.debug("Attempting to send email.");

        var mimeMessage = this.javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        var context = new Context();
        context.setVariables(Map.of(
                "username", receiver,
                "activation_code", activationCode
        ));

        helper.setFrom(senderEmail);
        helper.setTo(receiver);
        helper.setSubject(receiver);

        var template = this.templateEngine.process(templateName, context);

        helper.setText(template, true);

        this.javaMailSender.send(mimeMessage);

        log.info("Email has been sent.");
    }
}

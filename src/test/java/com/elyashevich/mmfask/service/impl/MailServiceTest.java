package com.elyashevich.mmfask.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() throws MessagingException {
        MockitoAnnotations.openMocks(this);

        // Mock MimeMessage
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @ParameterizedTest
    @MethodSource("provideValidMessageInputs")
    void sendMessage_ValidInputs_EmailSentSuccessfully(
            final String receiver,
            final String activationCode,
            final String templateName,
            final String expectedTemplateContent
    ) throws MessagingException {
        // Arrange
        var contextCaptor = ArgumentCaptor.forClass(Context.class);
        when(templateEngine.process(eq(templateName), contextCaptor.capture())).thenReturn(expectedTemplateContent);

        // Act
        mailService.setSenderEmail("fake-address@example.com");
        mailService.sendMessage(receiver, activationCode, templateName);

        // Assert
        verify(javaMailSender).send(mimeMessage);

        // Verify captured context variables
        var capturedContext = contextCaptor.getValue();
        assertAll(
                () -> assertEquals(activationCode, capturedContext.getVariable("activation_code")),
                () -> assertEquals(receiver, capturedContext.getVariable("username"))
        );

        // Verify MimeMessageHelper interactions
        verify(templateEngine).process(eq(templateName), any(Context.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMessageInputs")
    void sendMessage_InvalidInputs_ThrowsMessagingException(
            final String receiver,
            final String activationCode,
            final String templateName
    ) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mailService.sendMessage(receiver, activationCode, templateName));
    }

    @ParameterizedTest
    @MethodSource("provideTemplateEngineErrorInputs")
    void sendMessage_TemplateEngineThrowsException_ThrowsRuntimeException(
            final String receiver,
            final String activationCode,
            final String templateName
    ) {
        // Arrange
        when(templateEngine.process(eq(templateName), any(Context.class))).thenThrow(new RuntimeException("Template not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> mailService.sendMessage(receiver, activationCode, templateName));
    }

    // Data Providers

    static Stream<Arguments> provideValidMessageInputs() {
        return Stream.of(
                Arguments.of("test@example.com", "123456", "activation-template.html", "<html>Email Content</html>"),
                Arguments.of("user2@example.com", "789012", "welcome-template.html", "<html>Welcome Email</html>")
        );
    }

    static Stream<Arguments> provideInvalidMessageInputs() {
        return Stream.of(
                Arguments.of("invalid-email", "123456", "activation-template.html"),
                Arguments.of("", "789012", "welcome-template.html")
        );
    }

    static Stream<Arguments> provideTemplateEngineErrorInputs() {
        return Stream.of(
                Arguments.of("test@example.com", "123456", "non-existent-template.html"),
                Arguments.of("user2@example.com", "789012", "broken-template.html")
        );
    }
}

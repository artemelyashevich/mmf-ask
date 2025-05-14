package com.elyashevich.mmfask.util;

import com.elyashevich.mmfask.exception.InvalidTokenException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;


class SafetyExtractEmailUtilTest {

    private MockedStatic<TokenUtil> mockedTokenUtil;

    @BeforeEach
    void setUp() {
        mockedTokenUtil = mockStatic(TokenUtil.class);
    }

    @AfterEach
    void tearDown() {
        mockedTokenUtil.close();
    }

    @ParameterizedTest
    @CsvSource({
            "valid.jwt.token, test@example.com",
            "another.valid.jwt.token, another@example.com"
    })
    void extractEmailClaims_ValidJwt_ReturnsExpectedEmail(final String jwt, final String expectedEmail) {
        // Arrange
        mockedTokenUtil.when(() -> TokenUtil.extractEmailClaims(jwt)).thenReturn(expectedEmail);

        // Act
        var actualEmail = SafetyExtractEmailUtil.extractEmailClaims(jwt);

        // Assert
        assertEquals(expectedEmail, actualEmail);
        mockedTokenUtil.verify(() -> TokenUtil.extractEmailClaims(jwt), atLeastOnce());
    }

    @ParameterizedTest
    @CsvSource({
            "expired.jwt.token, JWT expired at",
            "malformed.jwt.token, Malformed JWT",
            "unsupported.jwt.token, Unsupported JWT",
            "empty.jwt.token, JWT claims string is empty or invalid"
    })
    void extractEmailClaims_InvalidJwt_ThrowsInvalidTokenException(final String jwt, final String expectedMessage) {
        // Arrange
        mockedTokenUtil.when(
                () -> TokenUtil.extractEmailClaims(jwt)).thenThrow(new IllegalArgumentException(expectedMessage)
        );

        // Act
        var exception = assertThrows(
                InvalidTokenException.class, () -> SafetyExtractEmailUtil.extractEmailClaims(jwt)
        );

        // Assert
        assertTrue(exception.getMessage().contains(expectedMessage));
        mockedTokenUtil.verify(() -> TokenUtil.extractEmailClaims(jwt), atLeastOnce());
    }

    @ParameterizedTest
    @CsvSource({
            "signature.jwt.token, JWT signature does not match locally computed signature"
    })
    void extractEmailClaims_ThrowsInvalidSignatureTokenException(final String jwt, final String expectedMessage) {
        // Arrange
        mockedTokenUtil.when(
                () -> TokenUtil.extractEmailClaims(jwt)).thenThrow(new SignatureException(expectedMessage)
        );

        // Act
        var exception = assertThrows(
                InvalidTokenException.class, () -> SafetyExtractEmailUtil.extractEmailClaims(jwt)
        );

        // Assert
        assertTrue(exception.getMessage().contains(expectedMessage));
        mockedTokenUtil.verify(() -> TokenUtil.extractEmailClaims(jwt), atLeastOnce());
    }

    @ParameterizedTest
    @CsvSource({
            "malformed.jwt.token, Malformed JWT"
    })
    void extractEmailClaims_MalformedJwtException_ThrowsInvalidTokenException(final String jwt, final String expectedMessage) {
        // Arrange
        mockedTokenUtil.when(
                () -> TokenUtil.extractEmailClaims(jwt)).thenThrow(new MalformedJwtException(expectedMessage)
        );

        // Act
        var exception = assertThrows(
                InvalidTokenException.class, () -> SafetyExtractEmailUtil.extractEmailClaims(jwt)
        );

        // Assert
        assertTrue(exception.getMessage().contains(expectedMessage));
        mockedTokenUtil.verify(() -> TokenUtil.extractEmailClaims(jwt), atLeastOnce());
    }

    @ParameterizedTest
    @CsvSource({
            "unsupported.jwt.token, Unsupported JWT format"
    })
    void extractEmailClaims_ThrowsInvalidTokenException(final String jwt, final String expectedMessage) {
        // Arrange
        mockedTokenUtil.when(
                () -> TokenUtil.extractEmailClaims(jwt)).thenThrow(new UnsupportedJwtException(expectedMessage)
        );

        // Act
        var exception = assertThrows(
                InvalidTokenException.class, () -> SafetyExtractEmailUtil.extractEmailClaims(jwt)
        );

        // Assert
        assertTrue(exception.getMessage().contains(expectedMessage));
        mockedTokenUtil.verify(() -> TokenUtil.extractEmailClaims(jwt), atLeastOnce());
    }
}
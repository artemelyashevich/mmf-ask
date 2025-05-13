package com.elyashevich.mmfask.util;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


class JsonMessageProviderUtilTest {

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(value = {
            "Test message|{\"message\":\"Test message\",\"errors\":{}}",
            "Another message|{\"message\":\"Another message\",\"errors\":{}}",
    }, delimiter = '|')
    void provide_ValidMessage_ReturnsJsonString(final String message, final String expectedJson) {
        var actualJson = JsonMessageProviderUtil.provide(message);
        assertEquals(expectedJson, actualJson);
    }
}
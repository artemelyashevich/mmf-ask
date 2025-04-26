package com.elyashevich.mmfask.util;

import com.elyashevich.mmfask.api.dto.exception.ExceptionBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class JsonMessageProviderUtil {

    public static String provide(String message) throws IOException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new ExceptionBody(message));
    }
}
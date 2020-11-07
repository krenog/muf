package com.krenog.myf.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krenog.myf.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestConverter {
    public static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    public static void checkTestErrorCode(MvcResult result, String code) throws IOException {
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse mainErrorDto = mapFromJson(content, ErrorResponse.class);
        Assertions.assertEquals(code, mainErrorDto.getCode());
    }
}

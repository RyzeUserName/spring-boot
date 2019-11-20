package com.example.formatterspringbootstarter.autoconfigure.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json 格式化
 * @author Ryze
 * @date 2019-11-20 18:43
 */
public class JsonFormatter implements Formatter {
    private final ObjectMapper objectMapper;

    public JsonFormatter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String formatter(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

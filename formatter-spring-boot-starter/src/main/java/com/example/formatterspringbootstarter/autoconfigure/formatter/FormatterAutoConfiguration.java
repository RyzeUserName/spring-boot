package com.example.formatterspringbootstarter.autoconfigure.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 */
@Configuration
@AutoConfigureAfter(value = JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "formatter", name = "enable", havingValue = "true",matchIfMissing =true )
@ConditionalOnResource(resources = "1.properties")
public class FormatterAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass(value = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    @ConditionalOnMissingBean(type = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter jsonFormatter() {
        return new JsonFormatter();
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public Formatter ObjectMapperFormatter(ObjectMapper objectMapper) {
        return new JsonFormatter(objectMapper);
    }
}

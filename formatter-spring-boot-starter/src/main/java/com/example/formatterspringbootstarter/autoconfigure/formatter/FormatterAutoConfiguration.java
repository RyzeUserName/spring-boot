package com.example.formatterspringbootstarter.autoconfigure.formatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 */
@Configuration
public class FormatterAutoConfiguration {
    @Bean
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }
}

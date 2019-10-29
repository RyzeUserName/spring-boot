package com.example.boothello.enableTest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 导入的类
 * @author Ryze
 * @date 2019-10-29 11:28
 */
@Configuration
public class HelloWorld {
    @Bean
    public String helloWorld() {
        return "hello world";
    }
}

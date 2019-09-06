package com.example.boothello;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class BootHelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootHelloApplication.class, args);
    }

//    /**
//     * 非web 环境下 WebServerApplicationContext 会被注入失败
//     * @param applicationContext
//     * @return
//     */
//    @Bean
//    public ApplicationRunner runner(WebServerApplicationContext applicationContext) {
//        return args -> System.out.println("当前webServer实现" + applicationContext.getWebServer().getClass().getName());
//    }
    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerRead(WebServerInitializedEvent webServerInitializedEvent) {
        System.out.println("当前webServer实现" + webServerInitializedEvent.getWebServer().getClass().getName());
    }

}

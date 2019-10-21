package com.example.boothello;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.EventListener;

@SpringBootApplication
//@SpringBootConfiguration
//@EnableAutoConfiguration
//@ComponentScan(excludeFilters = {
//    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
//    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class BootHelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootHelloApplication.class, args);
//        Class<BootHelloApplication> bootHelloApplicationClass = BootHelloApplication.class;
//        //非 web 环境
//        ConfigurableApplicationContext context = new SpringApplicationBuilder(bootHelloApplicationClass).web(WebApplicationType.NONE).run();
//        System.out.println("当前引导类" + context.getBean(bootHelloApplicationClass));
//        context.close();

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

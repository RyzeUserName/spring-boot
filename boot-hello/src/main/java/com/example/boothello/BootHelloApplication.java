package com.example.boothello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
//@SpringBootConfiguration
//@EnableAutoConfiguration
//@ComponentScan(excludeFilters = {
//    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
//    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
public class BootHelloApplication {

    public static void main(String[] args) throws IOException {
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
//    @EventListener(WebServerInitializedEvent.class)
//    public void onWebServerRead(WebServerInitializedEvent webServerInitializedEvent) {
//        System.out.println("当前webServer实现" + webServerInitializedEvent.getWebServer().getClass().getName());
//    }

}

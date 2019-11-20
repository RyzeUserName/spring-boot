package com.example.boothello.listener;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动类
 * @author Ryze
 * @date 2019-11-04 16:01
 */
@EnableAutoConfiguration(exclude = SpringApplicationAdminJmxAutoConfiguration.class)
public class ConditionBootStrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConditionBootStrap.class)
            .web(WebApplicationType.NONE)
            .run(args)
            .close();
    }
}

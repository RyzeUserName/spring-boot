package com.example.boothello.condition;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动类
 * @author Ryze
 * @date 2019-11-04 16:01
 */
@ComponentScan(basePackageClasses = ConditionMessageConfiguration.class)
@Configuration
public class ConditionBootStrap {

    public static void main(String[] args) {
        //设置系统变量
        System.setProperty("language", "English");
        //上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //注册 bean
        context.register(ConditionMessageConfiguration.class);
        //启动上下文
        context.refresh();
        //获取 message 的bean
        String message = context.getBean("message", String.class);
        System.out.println(message);
        //关闭上下文
        context.close();
    }
}

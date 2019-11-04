package com.example.boothello.profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 启动类
 * @author Ryze
 * @date 2019-11-04 16:01
 */
@ComponentScan(basePackageClasses = Calculate.class)
@Configuration
public class CalculateBootStrap {
    static {
        //设置系统变量
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "java8");

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CalculateBootStrap.class);
//        设置环境变量 类似静态代码块 设置
//        ConfigurableEnvironment environment = context.getEnvironment();
//        environment.setActiveProfiles("java7");
        context.refresh();
        Calculate bean = context.getBean(Calculate.class);
        bean.sum(1, 2, 3, 4);
        context.close();
    }
}

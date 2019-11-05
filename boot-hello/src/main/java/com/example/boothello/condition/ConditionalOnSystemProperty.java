package com.example.boothello.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 跟据 java 系统属性设置的语言，来加载不同的message
 * @author Ryze
 * @date 2019-11-05 10:10:12
 * @version V1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnConditionalOnSystemProperty.class)
public @interface ConditionalOnSystemProperty {
    /**
     * 名字
     */
    String name();

    /**
     * 值
     */
    String value();
}


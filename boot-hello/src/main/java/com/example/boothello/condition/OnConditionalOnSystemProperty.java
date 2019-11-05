package com.example.boothello.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

/**
 * 实现 比对
 * @author Ryze
 * @date 2019-11-05 10:11
 */
public class OnConditionalOnSystemProperty implements Condition {
    /**
     * 是否匹配
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //全部注解的值
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnSystemProperty.class.getName());
        //单值获取
        String name = (String) attributes.getFirst("name");
        String value = (String) attributes.getFirst("value");
        //获取java 属性的值
        String property = System.getProperty(name);
        //比较是否相等 匹配
        if (Objects.equals(value, property)) {
            return true;
        }
        return false;
    }
}

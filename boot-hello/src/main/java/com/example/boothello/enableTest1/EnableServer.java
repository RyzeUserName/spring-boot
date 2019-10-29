package com.example.boothello.enableTest1;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * enable 模块
 * @author Ryze
 * @date 2019-10-29 14:42:28
 * @version V1.0.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Import(ServerImportSelector.class) //如下的选择器
@Import(ServerImportBeanDefinitionRegistrar.class) //替换成选择器
public @interface EnableServer {
    /**
     * 设置服务器类型
     */
    Server.Type type();
}

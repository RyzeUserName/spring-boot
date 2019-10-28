package com.example.boothello.enumTest3;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
@Service("txService")
/**
 * 注解测试  spring 读取
 * @author Ryze
 */
public @interface MyAnnotation1 {
    /**
     * @return 名字
     */
    @AliasFor("value")
    String name() default "txManager";

//    /**
//     * @return 事务管理
//     */
//    String transactionManager() default "txManager";

    /**
     * @return 值
     */
    @AliasFor("name")
    String value() default "txManager";
}

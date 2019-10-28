package com.example.boothello.enumTest2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service("wwwwwwwwwww")
@Transactional
public @interface MyAnnotion1 {
    String name() default "";
}

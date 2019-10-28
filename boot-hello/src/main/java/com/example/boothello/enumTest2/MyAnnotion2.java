package com.example.boothello.enumTest2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service("wwwwwwwwwww")
@Transactional("tx")
public @interface MyAnnotion2 {
    String name() default "";
}

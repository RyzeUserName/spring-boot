package com.example.boothello.enableTest;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HelloWorld.class)
public @interface EnableHelloWorld {
}

package com.example.boothello.enumTest2;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 *　测试注解　java 方式　　
 * @author Ryze
 * @date 2019-10-22 11:17
 */
@MyAnnotion(name = "名字")
public class Test2 {
    public static void main(String[] args) {
        AnnotatedElement annotatedElement = Test2.class;
        MyAnnotion annotation = annotatedElement.getAnnotation(MyAnnotion.class);
        String name = annotation.name();
        System.out.println("值是 --> " + name);
        System.out.println("===========================");
        ReflectionUtils.doWithMethods(MyAnnotion.class,
            method -> System.out.println("方法名 -->" + method.getName()+"  值-->"+ReflectionUtils.invokeMethod(method,annotation)),
            method -> !method.getDeclaringClass().equals(Annotation.class));
    }
}

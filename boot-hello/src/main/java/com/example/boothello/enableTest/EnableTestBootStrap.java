//package com.example.boothello.enableTest;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 启动类
// * @author Ryze
// * @date 2019-10-28 18:10
// */
//@Configuration
//@EnableHelloWorld
//public class EnableTestBootStrap {
//    public static void main(String[] args) {
//        //注册当前引导类
//        AnnotationConfigApplicationContext bootStrap = new AnnotationConfigApplicationContext(EnableTestBootStrap.class);
//        //获取 名为helloWorld 的bean
//        String helloWorld = bootStrap.getBean("helloWorld", String.class);
//        System.out.println(helloWorld);
//    }
//
//}

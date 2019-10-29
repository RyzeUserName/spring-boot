package com.example.boothello.enableTest1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 启动类
 * @author Ryze
 * @date 2019-10-28 18:10
 */
@Configuration
@EnableServer(type = Server.Type.HTTP)
public class EnableTestBootStrap {
    public static void main(String[] args) {
        //注册当前引导类
        AnnotationConfigApplicationContext bootStrap = new AnnotationConfigApplicationContext(EnableTestBootStrap.class);
        //获取 Server的bean
        Server bean = bootStrap.getBean(Server.class);
        bean.start();
        bean.stop();
    }

}

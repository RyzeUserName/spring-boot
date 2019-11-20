package com.example.boothello.starterTest1;

import com.example.formatterspringbootstarter.autoconfigure.formatter.Formatter;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 引导启动类
 * @author Ryze
 * @date 2019-11-20 16:59
 */
@EnableAutoConfiguration
public class FormatterBootStrap {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplicationBuilder(FormatterBootStrap.class)
            .web(WebApplicationType.NONE)
            .run(args);

        Map<String, Object> map = new HashMap<>();
        map.put("测试", "格式化");
        Formatter bean = run.getBean(Formatter.class);
        String formatter = bean.formatter(map);
        System.out.println(formatter);
        run.close();
    }
}

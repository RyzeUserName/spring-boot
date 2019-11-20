package com.example.boothello.listener;

import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.Set;

/**
 * 自定义实现
 * @author Ryze
 * @date 2019-11-15 20:54
 */
public class MyListener implements AutoConfigurationImportListener {
    /**
     * Handle an auto-configuration import event.
     * @param event the event to respond to
     */
    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {
        //当前 classLoader
        ClassLoader classLoader = event.getClass().getClassLoader();
        //加载资源 k
        List<String> strings = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);
        //实际装载
        List<String> candidateConfigurations = event.getCandidateConfigurations();
        //获取排除的名单
        Set<String> exclusions = event.getExclusions();
        //输出信息
        System.out.printf("自动装配 Class 名单-候选数量:%d,实际数量:%d,排除数量:%d", strings.size(), candidateConfigurations.size(), exclusions.size());
        System.out.println();
        System.out.println("实际装载class：");
        candidateConfigurations.forEach(System.out::println);
        System.out.println("排除class：");
        exclusions.forEach(System.out::println);
    }
}

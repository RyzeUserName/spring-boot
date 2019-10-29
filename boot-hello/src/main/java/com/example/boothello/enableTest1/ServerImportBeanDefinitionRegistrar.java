package com.example.boothello.enableTest1;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.stream.Stream;

/**
 * 实现 ImportBeanDefinitionRegistrar
 * @author Ryze
 * @date 2019-10-29 14:47
 */
public class ServerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //创建选择器
        ServerImportSelector serverImportSelector = new ServerImportSelector();
        //获取创建bean 合集
        String[] strings = serverImportSelector.selectImports(importingClassMetadata);
        //注册
        Stream.of(strings)
            //转成BeanDefinitionBuilder
            .map(BeanDefinitionBuilder::genericBeanDefinition)
            //转成AbstractBeanDefinition
            .map(BeanDefinitionBuilder::getBeanDefinition)
            //注册 beanDefinition 到BeanDefinitionRegistry
            .forEach(beanDefinition -> BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry));
    }
}

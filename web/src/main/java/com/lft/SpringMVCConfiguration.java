package com.lft;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *  web 配置
 * @author Ryze
 * @date 2019-10-31 11:42
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = SpringMVCConfiguration.class)
public class SpringMVCConfiguration {
}

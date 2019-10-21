# spring-boot

# 1.总览

​	spring ioc 实现方式JNDI  DI是EJB容器的注入

​	spring aop 事务简化

​	spring mvc  

# 2.入门



创建springboot 项目

打成jar包，解压  META-INF目录下 MANIFEST.MF 

![ image ](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567653220045.png)

org.springframework.boot.loader.JarLauncher   jar 文件的

org.springframework.boot.loader.WarLauncher   war文件的启动器

引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-loader -->
<dependency>    
    <groupId>org.springframework.boot</groupId>   
    <artifactId>spring-boot-loader</artifactId>    
    <scope>provided</scope>
</dependency>
```



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567654046850.png)

支持jar启动和文件系统两种启动方式

结构如下：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567654475580.png)

main方法点进去

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669076427.png)

**第一句**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669133123.png)

设置值：k   v（追加 springboot.loader） 清空URLStreamHandler

发现：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669344118.png)

也就是说：URLStreamHandler 对应着不同的 protocol 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669494641.png)

​	去查看 org.springframework.boot.loader.jar.Handler  注释写着 for Spring Boot loader {@link JarFile}s.

​	而JarFile  扩展了java.util.JarFile   也就是 springboot项目jar文件的抽象

​	org.springframework.boot.loader.jar.Handler 就是用于处理 jar文件的内建工作(替换 sun.net.www.protocol.jar的jar 内

​	建)，话说为什么要替换呢？（可以试试 java xxx 看是否可以启动）是因为 springboot的 jar/war 除了传统的Java JAR

​	中的资源外，还包含以来的JAR文件，也就是说  springboot的 jar/war  是个独立的应用归档文件

​	

​	扩展：下面代码是如何获取URLStreamHandler 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567670589618.png)

​	

**第二句**： getClassPathArchives()  Archive资源  进而获取类加载器

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567672332232.png)

**第三句**：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567672972248.png)

JarLauncher 实际上是同进程调用Start-Class的main方法

WarLauncher  跟JarLauncher 区别不大 文件结构不同

将项目打成war

```xml
<packaging>war</packaging>
```

需要依赖：

```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

如图：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567673740857.png)

打包成WAR文件是一种兼容措施，既能WarLauncher   也能Servlet容器，建议使用费WEB部署，尽可能使用JAR归档方式

# 3.依赖

spring-boot-starter-parent  springboot 插件，版本管理（简化pom管理）

spring-boot-starter-dependencies  各种依赖

可以自定义parent 建议继承 spring-boot-starter-parent 

# 4.嵌入式容器

**servlet容器**

1.tomcat

2.Jetty

3.Undertow

**Reactive Web 容器**

以上容器也可做Reactive Web 容器，默认实现是Netty Web Server

4.webFlux （Reactor/Netty 整合实现）

springboot 可以通过指定容器的Maven 依赖切换容器，无需代码调整

加入代码，查看当前应用容器的实现

```java
//    /**
//     * 非web 环境下 WebServerApplicationContext 会被注入失败
//     * @param applicationContext
//     * @return
//     */
//    @Bean
//    public ApplicationRunner runner(WebServerApplicationContext applicationContext) {
//        return args -> System.out.println("当前webServer实现" + applicationContext.getWebServer().getClass().getName());
//    }

@EventListener(WebServerInitializedEvent.class)
public void onWebServerRead(WebServerInitializedEvent webServerInitializedEvent) {    System.out.println("当前webServer实现" + 		  webServerInitializedEvent.getWebServer().getClass().getName());
 }
```

## 1.嵌入式Servlet Web容器


### 1.Tomcat

Tomcat的Maven插件并非 嵌入式的容器，而是将WAR包归档到ROOT目录而已

springboot的默认是嵌入式tomcat

### 2.Jetty

修改依赖：

```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-web</artifactId>    
    <exclusions>        
        <exclusion>            
            <groupId>org.springframework.boot</groupId>            
            <artifactId>spring-boot-starter-tomcat</artifactId>        
        </exclusion>    
    </exclusions>
</dependency>
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

### 3.Undertow

修改依赖：

```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-web</artifactId>    
    <exclusions>        
        <exclusion>            
            <groupId>org.springframework.boot</groupId>            
            <artifactId>spring-boot-starter-tomcat</artifactId>        
        </exclusion>    
    </exclusions>
</dependency>
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

## 2.嵌入式Reactive Web容器

注意：spring-boot-starter-web 依赖和 spring-boot-starter-webflux依赖同时共存，后者会被忽略

所以，先去掉 spring-boot-starter-web 依赖

### 1.Undertow

修改依赖：

```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

### 2.Jetty

修改依赖:
```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

### 3.Tomcat

修改依赖：

```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-tomcat</artifactId>
</dependency>
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

# 5.自动装配

从注解 @SpringBootApplication开始 

点开查看到   

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568195369100.png)

@AliasFor 桥接其他注解的关键，也就是起了个别名而已。

故 @SpringBootApplication 是个组合注解，把这三个注解替换掉 注解SpringBootApplication，启动正常，没有区别

## 1.@ComponentScan 注解

表示扫描的范围,xml 中的 < context:component-scan > ，没写扫描范围（ basePackages） 默认是打这个注解的类所在

包 

FilterType.CUSTOM  使用自定义的过滤器处理

TypeExcludeFilter -> 把过滤器都 弄出来作为代理执行对象 过滤

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568197614539.png)

AutoConfigurationExcludeFilter

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568197776836.png)

## 2.@SpringBootConfiguration

SpringBootConfiguration   派生自 Configuration

Configuration  派生自  Component

##  3.@EnableAutoConfiguration

开启自动装配



注意：@Configuration 中的@Bean 与 @Component 中的@Bean 区别在于，Component 中为Lite模式

Configuration 中为Cglib 加强



结合使用   @ConditionalOnMissingBean  （备用的意思->当标注的目标类不存在 则初始化）

​				 @ConditionalOnClass  （优先什么）

​	 当以上类标注在@Configuration 类上，当且当该类在Class path下才予以装配

 

​	以HSQLDB为例：

​		查看spring boot 依赖 org.springframework.boot.autoconfigure 顾名思义就是自动装配

​		打开包 找到jdbc包 第一看到DataSourceAutoConfiguration   顾名思义 数据源的自动装配

​		![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568772241327.png)	

​	导入的类 ：

​	![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568772287670.png)

​	枚举：

​	![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568772589838.png)

​	那么 DataSourceAutoConfiguration    怎么装配的呢？

​	![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568773976826.png)

  很容易找到 web 自动装配

EmbeddedWebServerFactoryCustomizerAutoConfiguration

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568775996622.png)

# 6.Production-Ready

Spring Boot Actuator: Production-ready features

Spring Boot includes a number of additional features to help you monitor and manage your application when you push it to production. You can choose to manage and monitor your application by using HTTP endpoints or with JMX. Auditing, health, and metrics gathering can also be automatically applied to your application.

监管媒介：HTTP 或者 JMX.

端点类型：Auditing（审计）, health（健康）, and metrics gathering （指标收集）



引入依赖：

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

# 7.走向注解驱动编程

​	自java 5 注解 发布，spring 就开始了 自己的注解之路，逐步替换xml

## 	1.核心注解场景分类：

​		

| Spring 注解      | 场景                                     | 起始版本 |
| ---------------- | ---------------------------------------- | -------- |
| @Repository      | dao层模式注解                            | 2.0      |
| @Component       | 通用组件模式注解                         | 2.5      |
| @Service         | Service层注解                            | 2.5      |
| @Controller      | web层注解                                | 2.5      |
| @Configuration   | 配置类模式注解                           | 3.0      |
| @ImportResource  | 指示一个或多个包含要导入的bean定义的资源 | 2.5      |
| @Import          | 导入                                     | 2.5      |
| @ComponentScan   | 扫描指定目录下的类                       | 3.1      |
| @Autowired       | bean 依赖注入                            | 2.5      |
| @Qualifier       | 细粒度的Autowired 使用                   | 2.5      |
| @Bean            | bean                                     | 3.0      |
| @DependsOn       | 控制bean加载顺序                         | 3.0      |
| @Lazy            | 懒加载                                   | 3.0      |
| @Primary         | 优先级                                   | 3.0      |
| @Role            | 指明角色                                 | 3.1      |
| @Lookup          | 非单例注入                               | 4.1      |
| @Profile         | 配置化条件装配                           | 3.1      |
| @Conditional     | 编程条件装配                             | 3.1      |
| @PropertySource  | 配置属性抽象                             | 3.1      |
| @PropertySources | PropertySource 集合                      | 4.0      |
| @PostConstruct   | bean 生命周期的 初始化前                 | 2.5      |
| @PreDestory      | bean 生命周期的 销毁前                   | 2.5      |
| @AliasFor        | 别名                                     | 4.2      |
| @Indexed         | 索引注解                                 | 5.0      |
## 2.注解编程模型

### 	1.元注解

​	java 的 元注解  @Target  @Retention  @Documented  以及java8的@Repeatable

​	Spring 场景下 就是  @Component  

### 	2.Spirng 模式注解

#### 		1."派生"

​		@Repository  @Service @Controller @RestController  @Configuration 等  均被   @Component  元注解 标

​		注称为 @Component   注解的“派生”，由于java 注解不允许继承，因此Spring 采用元注解 标注方式实现 注解间的

​		“派生”

#### 	2.“派生性”

​		1.注解的扫描是 从标签< context: component-scan> 开始的

​		自 Spring 2.0开始 xml的配置文件的文档结构从DTD 变成XML Schema，同时引入可扩展的XML编写机制

​		（也就是  自定义  xml schema  的 xsd文件  ，然后定义 标签解析器  将解析器 注册到  NamespaceHandlerSupport

​		在 META-INF  新建spring.handlers和spring.schemas文件

​		spring.schemas 文件 http\://www.bytebeats.com/schema/rpc/rpc.xsd=/META-INF/rpc.xsd   自定义的地址=xsd文件

​		spring.handlers 文件http\://www.bytebeats.com/schema/rpc=com.bytebeats.spring4.extension.xml.RpcNamespace

​		Handler      自定义的地址 =   注册解析器 的类 ）

​		2. component-scan 找到 是在 spring-context  spring.handlers  中

​		![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554645199.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554659141.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554667278.png)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554685040.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554694457.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554701360.png)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554708632.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554721030.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554729468.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554736593.png)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554743863.png)

**注意**：

1.ClassPathBeanDefinitionScanner 类的创建过程中

默认添加 Component 注解的扫描，那么标注了 Component 的都会被扫描到（Repository,Controller,Service） 等 

2.ClassPathBeanDefinitionScanner 支持自定义的类型过滤规则  例如 Dubbo 的service 就是通过自定义扫描规则 实现

3.其实，扫描的注解 还有java 的注解Named 以及 ManagedBean

#### 3.多层次”派生性“

注解 SpringBootApplication 	

​			-SpringBootConfiguration

​				-Configuration

​					-Component

​		修改 springboot 的启动类

```java
public class BootHelloApplication {

    public static void main(String[] args) {
        Class<BootHelloApplication> bootHelloApplicationClass = BootHelloApplication.class;
        //非 web 环境
        ConfigurableApplicationContext context = new SpringApplicationBuilder(bootHelloApplicationClass).web(WebApplicationType.NONE).run();
        System.out.println("当前引导类" + context.getBean(bootHelloApplicationClass));
        context.close();
    }
}
```

启动后 ：

当前引导类com.example.boothello.BootHelloApplication$$EnhancerBySpringCGLIB$$e650376@10b892d5

也就是说 Component 注解 有多层次功能                                                                                                 

**该特性在Spring 3.0 开始支持**

#### 4.派生性原理

还是上面的代码

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571648917786.png)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571648932784.png)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571648942553.png)

关于 注解处理

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571649768630.png)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571649777203.png)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571649786081.png)

RuntimeVisibleTypeAnnotations 父类的注解  （@Inherited ）

**注意：** Spring 4.0 之后开始支持 多层次递归注解（之前的（3.0后） 要么支持 2层 要么支持 一层）

### 	3.Spring 组合注解



### 	4.Spring 注解属性和覆盖



# 8.注解驱动设计模式

# 9.自动装配

# 10.初始化

# 11.运行

# 12.结束

# 13.退出


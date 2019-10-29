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

**注意：** Spring 4.0 之后开始支持 多层次递归注解（之前的（3.0后） 要么支持 两层 要么支持 一层）

### 	3.Spring 组合注解

所谓组合注解 就是将一个注解上标注其他的一个或者多个注解，组合出注解的意思



上一节，派生性原理 也看到，spring并不是通过反射来处理注解，而是通过ASM （classReader)，读取类资源，直接操作

其中字节码，获取相关元信息，同时便于Spring字节码提升，以下是类似spring 获取注解代码

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Transactional
public @interface MyAnnotion {
}

@MyAnnotion
public class Test1 {
    public static void main(String[] args) throws IOException {
        String name = Test1.class.getName();
        MetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(name);
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        annotationMetadata.getAnnotationTypes().forEach(type -> {
            Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(type);
            metaAnnotationTypes.forEach(s -> System.out.println("注解是 " + s));
        });
    }
}
```

```
注解是 org.springframework.stereotype.Service
注解是 org.springframework.stereotype.Component
注解是 org.springframework.stereotype.Indexed
注解是 org.springframework.transaction.annotation.Transactional
```

### 	4.Spring 注解别名和覆盖

##### 1.java 反射方式  获取属性值

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Transactional
public @interface MyAnnotion {
    String name() default "";
}

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

```

结果:

```
值是 --> 名字
===========================
方法名 -->name  值-->名字
```

反射的方式 递归 获取全部的注解和值

```java
/**
 * java 反射的方式 获取全部注解 和 值
 * @author Ryze
 * @date 2019-10-24 14:04
 */
@MyAnnotion
public class Test3 {
    public static void main(String[] args) {
        AnnotatedElement annotatedElement = Test3.class;
        MyAnnotion annotation = annotatedElement.getAnnotation(MyAnnotion.class);
        Set<Annotation> allAnnotions = getAllAnnotions(annotation);
        allAnnotions.forEach(Test3::printAnnotions);
    }

    static Set<Annotation> getAllAnnotions(Annotation annotation) {
        Annotation[] annotations = annotation.annotationType().getAnnotations();
        if (ObjectUtils.isEmpty(annotations)) {
            return Collections.emptySet();
        }
        //过滤掉  java.lang.annotation 包下的基础注解
        Set<Annotation> collect = Stream.of(annotations).filter(
            a -> !Target.class.getPackage().equals(a.annotationType().getPackage())
        ).collect(Collectors.toSet());
        //递归
        Set<Annotation> collect1 = collect.stream().map(Test3::getAllAnnotions).collect(HashSet::new, Set::addAll, Set::addAll);
        //加起来
        collect.addAll(collect1);
        return collect;
    }

    static void printAnnotions(Annotation annotation) {
        Class<? extends Annotation> aClass = annotation.annotationType();
        ReflectionUtils.doWithMethods(aClass,
            method -> System.out.println("注解-->" + aClass.getSimpleName() + "方法名 -->" + method.getName() + "  值-->" + ReflectionUtils.invokeMethod(method, annotation)),
            method -> !method.getDeclaringClass().equals(Annotation.class));
    }
}

```

```
注解-->Service方法名 -->value  值-->
注解-->Component方法名 -->value  值-->
注解-->Transactional方法名 -->value  值-->
注解-->Transactional方法名 -->readOnly  值-->false
注解-->Transactional方法名 -->isolation  值-->DEFAULT
注解-->Transactional方法名 -->rollbackFor  值-->[Ljava.lang.Class;@1b4fb997
注解-->Transactional方法名 -->propagation  值-->REQUIRED
注解-->Transactional方法名 -->noRollbackFor  值-->[Ljava.lang.Class;@deb6432
注解-->Transactional方法名 -->timeout  值-->-1
注解-->Transactional方法名 -->noRollbackForClassName  值-->[Ljava.lang.String;@28ba21f3
注解-->Transactional方法名 -->transactionManager  值-->
注解-->Transactional方法名 -->rollbackForClassName  值-->[Ljava.lang.String;@694f9431
```

spring AnnotationMetadata 实现获取注解 和 值（也是反射）

```java
/**
 * spring  AnnotationMetadata 实现获取注解 值  
 * @author Ryze
 * @date 2019-10-24 15:31
 */
@MyAnnotion
public class Test4 {
    public static void main(String[] args) {
        AnnotationMetadata metadata = new StandardAnnotationMetadata(Test4.class);
        Set<String> collect = metadata.getAnnotationTypes().stream().map(metadata::getMetaAnnotationTypes).collect(LinkedHashSet::new, Set::addAll, Set::addAll);
        collect.stream().forEach(m -> {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(m);
            if (!CollectionUtils.isEmpty(annotationAttributes)) {
                annotationAttributes.forEach((k, v) ->
                    System.out.println("注解 " + ClassUtils.getShortName(m) + "属性" + k + " = " + v));
            }
        });
    }
}

```

结果

```
注解 Service属性value = 
注解 Transactional属性value = 
注解 Transactional属性timeout = -1
注解 Transactional属性readOnly = false
注解 Transactional属性noRollbackForClassName = [Ljava.lang.String;@379619aa
注解 Transactional属性transactionManager = 
注解 Transactional属性rollbackForClassName = [Ljava.lang.String;@cac736f
注解 Transactional属性isolation = DEFAULT
注解 Transactional属性noRollbackFor = [Ljava.lang.Class;@5e265ba4
注解 Transactional属性rollbackFor = [Ljava.lang.Class;@156643d4
注解 Transactional属性propagation = REQUIRED
注解 Component属性value = 
```

为什么spring 要实现两种 注解处理方式呢？

基于java反射，需要的是类加载器（classLoader），然而 springboot 的自己归档的 jar 扫描加载 class时，根据注解才

扫描加载，显然classLoader 加载已经不适合，因此基于ASM的加载在默认的扫描实现中出现。当然效率方面也有差距

（asm 加载较快《作者测试，我并未校验，因素太多》），另外 StandardAnnotationMetadata 的方法调用链路较长

##### 2.属性值的覆盖

**java方式**

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service("wwwwwwwwwww")
@Transactional
public @interface MyAnnotion1 {
    String name() default "";
}

/**
 * spring  属性值的覆盖
 * @author Ryze
 * @date 2019-10-24 15:31
 */
@MyAnnotion1
public class Test5 {
    public static void main(String[] args) {
        AnnotationMetadata metadata = new StandardAnnotationMetadata(Test5.class);
        Set<String> collect = metadata.getAnnotationTypes().stream().map(metadata::getMetaAnnotationTypes).collect(LinkedHashSet::new, Set::addAll, Set::addAll);
        collect.stream().forEach(m -> {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(m);
            if (!CollectionUtils.isEmpty(annotationAttributes)) {
                annotationAttributes.forEach((k, v) ->
                    System.out.println("注解 " + ClassUtils.getShortName(m) + "属性" + k + " = " + v));
            }
        });
    }
}

```

结果：

```
注解 Service属性value = wwwwwwwwwww
注解 Transactional属性value = 
注解 Transactional属性readOnly = false
注解 Transactional属性timeout = -1
注解 Transactional属性transactionManager = 
注解 Transactional属性rollbackForClassName = [Ljava.lang.String;@379619aa
注解 Transactional属性noRollbackForClassName = [Ljava.lang.String;@cac736f
注解 Transactional属性rollbackFor = [Ljava.lang.Class;@5e265ba4
注解 Transactional属性isolation = DEFAULT
注解 Transactional属性noRollbackFor = [Ljava.lang.Class;@156643d4
注解 Transactional属性propagation = REQUIRED
注解 Component属性value = wwwwwwwwwww
```

看到service的 value 覆盖了Component  value ，我们查看  AnnotatedElementUtils 的 getMergedAnnotationAttributes

注释写着：  

```
Attributes from lower levels in the annotation hierarchy override attributes* of the same name from higher levels
注释层次结构中较低级别的属性会覆盖属性在更高级别的同名

```

也就是说 @service  比 @Component  更低级别

 getAnnotationAttributes 返回值是Map<String, Object>  同名属性必然会覆盖，最外层的覆盖最内层的

**spring 方式**

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
@Service("txService")
/**
 * 注解测试  spring 读取注解
 * @author Ryze
 */
public @interface MyAnnotation1 {
    /**
     * @return 名字
     */
    String name() default "";

    /**
     * @return 事务管理
     */
    String transactionManager() default "txManager";

}
/**
 * 测试注解测试类
 * @author Ryze
 * @date 2019-10-28 18:09
 */
@MyAnnotation1
public class TxService {
    public void save() {
        System.out.println("保存操作");
    }
}
/**
 * 启动类
 * @author Ryze
 * @date 2019-10-28 18:10
 */
@ComponentScan(basePackageClasses = TxService.class)
@EnableTransactionManagement
public class TxBootStrap {
    public static void main(String[] args) {
        //注册当前引导类
        AnnotationConfigApplicationContext bootStrap = new AnnotationConfigApplicationContext(TxBootStrap.class);
        //获取 TxService 的bean
        Map<String, TxService> beansOfType = bootStrap.getBeansOfType(TxService.class);
        beansOfType.forEach((name, txService) -> {
            System.out.printf("Bean 名称 : %s,对象: %s\n", name, txService);
            txService.save();
        });
    }

    @Bean("txManager")
    public PlatformTransactionManager txManager() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) throws TransactionException {
                System.out.println("txManager： 事务提交");
            }

            @Override
            public void rollback(TransactionStatus status) throws TransactionException {
                System.out.println("txManager： 事务回滚");
            }
        };
    }

    @Bean("txManager2")
    public PlatformTransactionManager txManager2() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) throws TransactionException {
                System.out.println("txManager2： 事务提交");
            }

            @Override
            public void rollback(TransactionStatus status) throws TransactionException {
                System.out.println("txManager2： 事务回滚");
            }
        };
    }
}





```

结果：

```
Bean 名称 : txService,对象: com.example.boothello.enumTest3.TxService@3c41ed1d
保存操作
txManager： 事务提交
```

看到  MyAnnotation1 的属性（方法） transactionManager 覆盖了 @Transactional 的同名 属性（方法）

##### 3.理解属性值覆盖

属性覆盖分为 隐式属性覆盖 （同名属性） 以上的例子 

​					   显示属性覆盖   （@AliasFor 属性覆盖）

我们着重 来看看 显示的属性覆盖

就@Transaction  而言  value  相对于transactionManager 更要有语义，于是看到

```java
@AliasFor("transactionManager")
	String value() default "";
@AliasFor("value")
	String transactionManager() default "";
```

也就是说 一个注解中两个属性 可以通过 @AliasFor 成对出现 命名别名，那么我们注释掉  MyAnnotation1  中的

transactionManager 属性，修改成 value属性

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
@Service("txService")
/**
 * 注解测试  spring 读取
 * @author Ryze
 */
public @interface MyAnnotation1 {
    /**
     * @return 名字
     */
    String name() default "";

//    /**
//     * @return 事务管理
//     */
//    String transactionManager() default "txManager";

    /**
     * @return 值
     */
    String value() default "txManager";
}
```

重新运行，并未打印出任何结果 看到日志中

```
09:58:35.047 [main] INFO org.springframework.beans.factory.support.DefaultListableBeanFactory - Overriding bean definition for bean 'txManager' with a different definition: replacing [Generic bean: class [com.example.boothello.enumTest3.TxService]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in file [E:\study\springboot\spring-boot\boot-hello\target\classes\com\example\boothello\enumTest3\TxService.class]] with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=txBootStrap; factoryMethodName=txManager; initMethodName=null; destroyMethodName=(inferred); defined in com.example.boothello.enumTest3.TxBootStrap]

```

显示  TxService 被 txManager 取代 ，没有了 TxService 的bean 所以打印不出来。

为什么会被取代？ MyAnnotation1 的 value  覆盖了 bean 的名字，成了 txManager  

使用显示覆盖 调整代码：

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
@Service("txService")
/**
 * 注解测试  spring 读取
 * @author Ryze
 */
public @interface MyAnnotation1 {
    /**
     * @return 名字
     */
    String name() default "";
    /**
     * @return 覆盖事务管理
     */
    @AliasFor(annotation = Transactional.class,value = "value")
    String tm() default "txManager";
}
```

运行结果：

```
Bean 名称 : txService,对象: com.example.boothello.enumTest3.TxService@38425407
保存操作
txManager： 事务提交
```

@AliasFor　通注解中 别名覆盖 需要成对出现，并且默认值相等，单向的别名覆盖，从低级向高级 别名覆盖。如上！

也就是说 spring 扩展了 注解

通过AnnotationAttributes 表达语义（扩展: @EnableDubbo 也是如此实现）

# 8.注解驱动设计模式

# 9.自动装配

# 10.初始化

# 11.运行

# 12.结束

# 13.退出


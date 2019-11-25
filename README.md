# spring-boot

# 1.总览

​	spring ioc 实现方式JNDI  DI是EJB容器的注入

​	spring aop 事务简化

​	spring mvc  

# 2.入门



创建springboot 项目

打成jar包，解压  META-INF目录下 MANIFEST.MF 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567653220045.png?raw=true)

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



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567654046850.png?raw=true)

支持jar启动和文件系统两种启动方式

结构如下：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567654475580.png?raw=true)

main方法点进去

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669076427.png?raw=true)

**第一句**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669133123.png?raw=true)

设置值：k   v（追加 springboot.loader） 清空URLStreamHandler

发现：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669344118.png?raw=true)

也就是说：URLStreamHandler 对应着不同的 protocol 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567669494641.png?raw=true)

​	去查看 org.springframework.boot.loader.jar.Handler  注释写着 for Spring Boot loader {@link JarFile}s.

​	而JarFile  扩展了java.util.JarFile   也就是 springboot项目jar文件的抽象

​	org.springframework.boot.loader.jar.Handler 就是用于处理 jar文件的内建工作(替换 sun.net.www.protocol.jar的jar 内

​	建)，话说为什么要替换呢？（可以试试 java xxx 看是否可以启动）是因为 springboot的 jar/war 除了传统的Java JAR

​	中的资源外，还包含以来的JAR文件，也就是说  springboot的 jar/war  是个独立的应用归档文件

​	

​	扩展：下面代码是如何获取URLStreamHandler 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567670589618.png?raw=true)

​	

**第二句**： getClassPathArchives()  Archive资源  进而获取类加载器

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567672332232.png?raw=true)

**第三句**：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567672972248.png?raw=true)

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

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1567673740857.png?raw=true)

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

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568195369100.png?raw=true)

@AliasFor 桥接其他注解的关键，也就是起了个别名而已。

故 @SpringBootApplication 是个组合注解，把这三个注解替换掉 注解SpringBootApplication，启动正常，没有区别

## 1.@ComponentScan 注解

表示扫描的范围,xml 中的 < context:component-scan > ，没写扫描范围（ basePackages） 默认是打这个注解的类所在

包 

FilterType.CUSTOM  使用自定义的过滤器处理

TypeExcludeFilter -> 把过滤器都 弄出来作为代理执行对象 过滤

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568197614539.png?raw=true)

AutoConfigurationExcludeFilter

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568197776836.png?raw=true)

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

​		![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568772241327.png?raw=true)	

​	导入的类 ：

​	![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568772287670.png?raw=true)

​	枚举：

​	![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568772589838.png?raw=true)

​	那么 DataSourceAutoConfiguration    怎么装配的呢？

​	![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568773976826.png?raw=true)

  很容易找到 web 自动装配

EmbeddedWebServerFactoryCustomizerAutoConfiguration

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1568775996622.png?raw=true)

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

​		![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554645199.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554659141.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554667278.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554685040.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554694457.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554701360.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554708632.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554721030.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554729468.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554736593.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571554743863.png?raw=true)

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

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571648917786.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571648932784.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571648942553.png?raw=true)

关于 注解处理

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571649768630.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571649777203.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1571649786081.png?raw=true)

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

## 1.@Enable 模块驱动

| 框架         | 模块                           | 激活的模块        |
| ------------ | ------------------------------ | ----------------- |
| Spring       | @EnableWebMvc                  | web mvc           |
| Spring       | @EnableTransactionManagement   | 事务管理          |
| Spring       | @EnableCaching                 | 缓存              |
| Spring       | @EnableMBeanExport             | JMX               |
| Spring       | @EnableAsync                   | 异步处理          |
| Spring       | @EnableWebFlux                 | web flux          |
| Spring       | @EnableAspectJAutoProxy        | AspectJ           |
| Spring boot  | @EnableAutoConfiguration       | 自动转配模块      |
| Spring boot  | @EnableManagementContext       | Actuate管理模块   |
| Spring boot  | @EnableConfigurationProperties | 配置属性绑定模块  |
| Spring boot  | @EnableOAuth2Sso               | Auth2 单点登录    |
| Spring cloud | @EnableEurekaServer            | eureka 服务器模块 |
| Spring cloud | @EnableConfigServer            | 配置服务器模块    |
| Spring cloud | @EnableFeignClients            | feign 客户端模块  |
| Spring cloud | @EnableZuulProxy               | zuul 模块         |
| Spring cloud | @EnableCircuitBreaker          | 服务熔断          |

### 1.理解

模块驱动的出现在于 简化装配步骤，实现“按需装配”，屏蔽实现细节。

### 2.自定义

spring 框架大致分成两种 ： 注解驱动  接口编程

**注解驱动**： @Configuration 类+ @Bean 方法  

查看 @EnableWebMvc 

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)  // 是个标注@Configuration 的类
public @interface EnableWebMvc {
}
```

仿写

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HelloWorld.class)
public @interface EnableHelloWorld {
}

@Configuration
public class HelloWorld {
    @Bean
    public String helloWorld() {
        return "hello world";
    }
}

/**
 * 启动类
 * @author Ryze
 * @date 2019-10-28 18:10
 */
@Configuration
@EnableHelloWorld
public class EnableTestBootStrap {
    public static void main(String[] args) {
        //注册当前引导类
        AnnotationConfigApplicationContext bootStrap = new AnnotationConfigApplicationContext(EnableTestBootStrap.class);
        //获取 名为helloWorld 的bean
        String helloWorld = bootStrap.getBean("helloWorld", String.class);
        System.out.println(helloWorld);
    }

}
```

结果：

```
hello world
```

**接口编程**：ImportSelector 或 ImportBeanDefinitionRegistrar 的实现类 

ImportSelector : 比较简单，导入选择器

ImportBeanDefinitionRegistrar ：除了ImportSelector  的功能，自己还需要注册bean

查看 @EnableCaching

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CachingConfigurationSelector.class) //如下的选择器 
public @interface EnableCaching {
...
}
// AdviceModeImportSelector   implements ImportSelector 
public class CachingConfigurationSelector extends AdviceModeImportSelector<EnableCaching> {
    	@Override
	public String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return getProxyImports();
			case ASPECTJ:
				return getAspectJImports();
			default:
				return null;
		}
	}
    ...
}
```

仿写:

```java
/**
 * 服务接口 假设 我们两种 服务: http、ftp
 * {@link HttpServer}
 * {@link FtpServer}
 * @author Ryze
 * @date 2019-10-29 14:24:54
 * @version V1.0.0
 */
public interface Server {
    enum Type {
        /**
         * 两种服务
         */
        HTTP, FTP
    }

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();
}

/**
 * http 实现
 * @author Ryze
 * @date 2019-10-29 14:27
 */
@Component
public class HttpServer implements Server {
    /**
     * 启动
     */
    @Override
    public void start() {
        System.out.println("HTTP 实现启动 ");
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        System.out.println("HTTP 实现停止 ");
    }
}
/**
 * ftp实现
 * @author Ryze
 * @date 2019-10-29 14:28
 */
@Component
public class FtpServer implements Server {
    /**
     * 启动
     */
    @Override
    public void start() {
        System.out.println("FTP 实现启动 ");
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        System.out.println("FTP 实现启动 ");
    }
}

/**
 * enable 模块
 * @author Ryze
 * @date 2019-10-29 14:42:28
 * @version V1.0.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServerImportSelector.class) //如下的选择器
public @interface EnableServer {
    /**
     * 设置服务器类型
     */
    Server.Type type();
}
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

```

输出结果：

```
HTTP 实现启动 
HTTP 实现停止 
```

ImportBeanDefinitionRegistrar  只需要在 以上的基础上 接着加入代码

```java
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

```

修改 @EnableServer

```java
/**
 * enable 模块
 * @author Ryze
 * @date 2019-10-29 14:42:28
 * @version V1.0.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Import(ServerImportSelector.class) //如下的选择器
@Import(ServerImportBeanDefinitionRegistrar.class) //替换以上的实现类
public @interface EnableServer {
    /**
     * 设置服务器类型
     */
    Server.Type type();
}

```

运行：

```
HTTP 实现启动 
HTTP 实现停止 
```

进阶阅读：@EnableDubboConfig

### 3.原理

@Enable 模块 无论自定义 还是 spring 自建的，均使用@Import 实现，@Configuration 标注的类、实现ImportSelector 

的类、实现ImportBeanDefinitionRegistrar的类

**1. @Configuration  的类**

查看注解注释

```
 * <p>As an alternative to registering {@code @Configuration} classes directly against an
 * {@code AnnotationConfigApplicationContext}, {@code @Configuration} classes may be
 * declared as normal {@code <bean>} definitions within Spring XML files:
 * <pre class="code">
 * {@code
 * <beans>
 *    <context:annotation-config/>
 *    <bean class="com.acme.AppConfig"/>
 * </beans>}</pre>
 *
 * In the example above, {@code <context:annotation-config/>} is required in order to
 * enable {@link ConfigurationClassPostProcessor} and other annotation-related
 * post processors that facilitate handling {@code @Configuration} classes.
```

也就说 @Configuration 等效于之前的 <context:annotation-config /> XML 配置的bean

最终会被 ConfigurationClassPostProcessor 注册

同上面查找方法：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572404989989.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572405004318.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572406498621.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572406508789.png?raw=true)

那么@Configuration 的注解 如上面 @ComponentScan 注解扫描 也是会调用

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572406623867.png?raw=true)

另外 还有AnnotationConfigApplicationContext 注解 驱动上下文实现，也会扫描@Configuration 从实现看到

其构造中的AnnotatedBeanDefinitionReader

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572419233184.png?raw=true)

也就是说，所有关于Configuration  注解的处理全部都归结到

AnnotationConfigUtils.registerAnnotationConfigProcessors

最终 包装成 ConfigurationClassPostProcessor ，查看注释：

```
 * This post processor is {@link Ordered#HIGHEST_PRECEDENCE} as it is important
 * that any {@link Bean} methods declared in Configuration classes have their
 * respective bean definitions registered before any other BeanFactoryPostProcessor
 * executes.
 最高级别的post processor 去处理  Configuration classes 也处理其他的bean
```

ConfigurationClassPostProcessor 在上下文刷新中 被加载成spring bean，其 postProcessBeanFactory 被调用

处理 @Configuration 和@Bean

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572429956496.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572429963852.png?raw=true)



![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572429978915.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572432359978.png?raw=true)

递归处理@import

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572432370562.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572432380258.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572432393553.png?raw=true)

最后用enhanceConfigurationClasses 加强config标注的类，回到第一张图

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572429956496.png?raw=true)

2.ImportSelector 和ImportBeanDefinitionRegistrar 实现类

以上的图片中就已包含了 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572432380258.png?raw=true)

candidate.isAssignable(ImportSelector.class) 是否是 ImportSelector 导入 （selector.selectImports）

candidate.isAssignable(ImportBeanDefinitionRegistrar.class) 是否是 ImportBeanDefinitionRegistrar 导入

（configClass.addImportBeanDefinitionRegistrar）

## 2.Web自动装配 

### 1.理解

查看spring web的官方文档

 初始化个 WebApplicationInitializer ，用于编程式取代web.xml,

```java
import org.springframework.web.WebApplicationInitializer;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");

        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet(appContext));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
```

但是 WebApplicationInitializer是 spring mvc的接口，如果不用这个的话，可以实现

AbstractDispatcherServletInitializer (基于编程式)     **或者**

AbstractDispatcherServletInitializer  (基于XML)

```java
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { MyWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}

public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        XmlWebApplicationContext cxt = new XmlWebApplicationContext();
        cxt.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");
        return cxt;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```



### 2.自定义

新建个web 项目

代码：

```java
/**
 * 测试controller
 * @author Ryze
 * @date 2019-10-31 11:38
 */
@Controller
public class HelloWorldController {

    @RequestMapping
    @ResponseBody
    public String helloWorld() {
        return "HELLO  WORLD";
    }
}

/**
 * web 初始化
 * @author Ryze
 * @date 2019-10-31 11:45
 */
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{SpringMVCConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
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

```

pom.xml

```xml
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring.version>3.2.18.RELEASE</spring.version>
    </properties>

    <dependencies>
        <!-- Spring 上下文依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!-- Spring 事务依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!-- Spring Web MVC 依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.1</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.5.1</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                    <fork>true</fork>
                </configuration>
            </plugin>
            <!-- Maven war 插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <configuration>
                    <!-- 忽略错误，当web.xml不存在时 -->
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>

            <!-- Tomcat Maven 插件用于构建可执行 war -->
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.1</version>
                <executions>
                    <execution>
                        <id>tomcat-run</id>
                        <goals>
                            <!-- 最终打包成可执行的jar包 -->
                            <goal>exec-war-only</goal>
                        </goals>
                        <phase>package</phase>
                        <configuration>
                            <!-- ServletContext 路径 -->
                            <path>/</path>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

之后执行 mvn clean package

进入target 目录执行： java -jar web-1.0-SNAPSHOT-war-exec.jar

访问 localhost: 8080   看到了  HELLO  WORLD 也就是说 ，我们已经完成了 web（spring mvc） 的自动装配

(web 目录下的项目)

### 3.原理

实际上 spring 框架并不具备 web自动装配的 原生能力，而是由于 Servlet 3.0技术中的  ServletContext 配置方法 和 运

行时插拔的两大特性

#### 1.ServletContext 配置方法

传统的web 项目，需要配置 web.xml 文件,配置 filter、servlet、listener 一旦配置好就不支持 修改和占位符 ，自 Servlet3.0之后，出现了ServletContext 为了弥补这些细节

具体的 api 需要查看 ServletContext 接口看到

addListener

addServlet

addFilter  

等 一些方法。ServletContext  为运行时提供了装配能力，但是时机还需要自己把握

#### 2.运行时插拔

规范中提及  

以上 ServletContext 新增的方法要么是在 ServletContextListener 的 contexInitialized 方法中调用，要么是在 ServletContainerInitializer 的 onStartup() 方法中调用。

**ServletContextListener**  负责监听 ServletContext 的生命周期事件 包括初始化和销毁，contexInitialized  方法就是对初始化的监听。

**ServletContainerInitializer** 也是 Servlet 3.0 新增的一个接口，容器在启动时使用 JAR 服务 API(JAR Service API) 来发现 ServletContainerInitializer 的实现类，并且容器将 WEB-INF/lib 目录下 JAR 包中的类都交给该类的 onStartup() 方法处理，我们通常需要在该实现类上使用 @HandlesTypes 注解来指定希望被处理的类，过滤掉不希望给 onStartup() 处理的类。

也就是说，ServletContainerInitializer  早于 ServletContextListener  

大致的流程：一个servlet 需要被装配，被提供服务，那么首先是  ServletContext # addServlet，为其装配，

随后到  ServletContainerInitializer # onStartup 实现，多个的话就是一样的过程。

ServletContainerInitializer  的实现类 SpringServletContainerInitializer

```java
@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)throws ServletException {
		...
	}

}

```

是对 WebApplicationInitializer的 实现类的处理 ，看到

-AbstractContextLoaderInitializer  (构建Web Root 应用上下文  WebApplicationContext， 替代web.xml 注册 

​															ContextLoaderListener)

​	-AbstractDispatcherServletInitializer （替代 web.xml 注册 DispatcherServlet，必要的话创建构建Web Root 应用上																	下文  WebApplicationContext）

​		-AbstractAnnotationConfigDispatcherServletInitializer（具备注解驱动的 AbstractDispatcherServletInitializer ）

下面 一 一介绍

#### 3.AbstractContextLoaderInitializer  

在其源码中看到

注册 ContextLoaderListener 到 ServletContext 如下：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572592884760.png?raw=true)

也就是说 先创建 root 上下文 ，注册监听，然后监听ServletContext 的初始化和销毁

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572593301298.png?raw=true)

当环境在servlet3.0以上，实现 AbstractContextLoaderInitializer 只需要实现  createRootApplicationContext 方法即可

但在Spring Mvc 使用场景下，直接实现 AbstractContextLoaderInitializer 的方式不推荐，所以没文档示例说明。

另外 ContextLoaderListener  不允许重复注册到  ServletContext  （原因下面说）先了解下：

在Spring Mvc 中  每个DispatcherServlet 都有 自己的 Servlet WebApplicationContext，继承自Root 

WebApplicationContext( 各种 Bean)

![image](https://docs.spring.io/spring/docs/5.2.0.RELEASE/spring-framework-reference/images/mvc-context-hierarchy.png)



也就是说 只有 一个 root 上下文，而创建上下文并 注册监听是在 onStartup 一个方法里，所以这就保证了 一个root上下文只有一个监听，在 ContextLoaderListener 的 contextInitialized 初始化 监听时, 调用父类ContextLoader 的contextInitialized ，代码如下：

```java
if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {throw new IllegalStateException(
					"Cannot initialize context because there is already a root application context present - " +
					"check whether you have multiple ContextLoader* definitions in your web.xml!");
		}
```

即 检查 root 上下文是否已经存在，存在的话  就报错。

也就是说   AbstractContextLoaderInitializer  的功能： 初始化 root 上下文（WebApplicationContext），注册监听

但是 createRootApplicationContext 没有实现，

 并未完成 前端派发（Dispatcher） 等核心 功能，当然我觉得 spring 就是想把这些个小功能都切开，等着别人多实现

#### 4.AbstractDispatcherServletInitializer 

弥补了父类没有  Dispatcher的遗憾，如下

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572597468296.png?raw=true)

customizeRegistration 可执行进一步定制注册

实现其成本 也不低，需要实现 父类的方法和 createServletApplicationContext 方法，也就引出了下面这个

#### 5.AbstractAnnotationConfigDispatcherServletInitializer

web自定义装配 章节 时实现的类。

实现了 上面的

AbstractContextLoaderInitializer 的 createRootApplicationContext

AbstractDispatcherServletInitializer  的  createServletApplicationContext

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572602598746.png?raw=true)

AnnotationConfigWebApplicationContext 需要注册 这些信息（配置类），从而驱动其他Bean的初始化，但是这两个类是可选的，子类必须覆盖，从而让开发人员感知存在，至于是否装配取决于应用的需要。上面的例子就是覆盖，但是并未实现 getServletMappings 方法源自 AbstractDispatcherServletInitializer，指定映射根路径

**总结：**

SpringServletContainerInitializer 通过实现 Servlet 3.0的 ServletContainerInitializer 与

@HandlesTypes() 顺序执行 WebApplicationInitializer 的实现集合，进而利用  Servlet 3.0 的api 实现web 的自动装配，

并结合 AnnotationConfigWebApplicationContext 的简化配置 ，实现简化的自动化装配。

## 3.Spring条件装配

通常会为不同部署环境 提供条件装配，大致的手段分为两种：

1. 编译时差异化（依赖外部的构建工具 Maven 等）

2. 利用不同环境的配置控制统一归档文件 （系统变量等）

spring 选择了后者，自3.1开始逐步引入 @Profile @Conditional

### 1.理解

 	即 application-dev.xml  application-test.xml  根据环境变量 加载不同的配置文件。

​	spring 允许设置两种类型，有效的、默认的（无有效的 选择默认的）

​	spring 应用有两种profile 配置的选择： ConfigurableEnvironment 、java 系统属性配置

| 设置类型            | ConfigurableEnvironment API | java系统属性            |
| ------------------- | --------------------------- | ----------------------- |
| 设置Active profile  | setActiveProfiles           | spring.profiles.active  |
| 添加Active profile  | addActiveProfile            |                         |
| 设置Default profile | setDefaultProfiles          | spring.profiles.default |

### 2.自定义

定义 一个接口不同环境，不同实现

```java
/**
 * 计算接口
 * @author Ryze
 * @date 2019-11-04 15:53
 */
public interface Calculate {
    /**
     * 累加 计算
     * @param integer 计算的参数
     * @return Integer sum
     * @author Ryze
     * @date 2019-11-04 15:54:27
     */
    Integer sum(Integer... integer);
}
/**
 * java7实现
 * @author Ryze
 * @date 2019-11-04 15:55
 */
@Service
@Profile("java7")
public class Java7CalculateImpl implements Calculate {
    /**
     * 累加 计算
     * @param integer 计算的参数
     * @return Integer sum
     * @author Ryze
     * @date 2019-11-04 15:54:27
     */
    @Override
    public Integer sum(Integer... integer) {
        Integer reduce = 0;
        for (Integer one : integer) {
            reduce += one;
        }
        System.out.printf("java7计算 %s 结果: %d\n", Arrays.asList(integer), reduce);
        return reduce;
    }
}
/**
 * java 8实现
 * @author Ryze
 * @date 2019-11-04 15:55
 */
@Service
@Profile("java8")
public class Java8CalculateImpl implements Calculate {
    /**
     * 累加 计算
     * @param integer 计算的参数
     * @return Integer sum
     * @author Ryze
     * @date 2019-11-04 15:54:27
     */
    @Override
    public Integer sum(Integer... integer) {
        Integer reduce = Stream.of(integer).reduce(0, Integer::sum);
        System.out.printf("java8计算 %s 结果: %d\n", Arrays.asList(integer), reduce);
        return reduce;
    }
}

```

定义启动类 并检测

```java
/**
 * 启动类
 * @author Ryze
 * @date 2019-11-04 16:01
 */
@ComponentScan(basePackageClasses = Calculate.class)
@Configuration
public class CalculateBootStrap {
    static {
        //设置系统变量
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "java8");

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CalculateBootStrap.class);
//        设置环境变量 类似静态代码块 设置
//        ConfigurableEnvironment environment = context.getEnvironment();
//        environment.setActiveProfiles("java7");
        context.refresh();
        Calculate bean = context.getBean(Calculate.class);
        bean.sum(1, 2, 3, 4);
        context.close();
    }
}

```

结果：

```
java8计算 [1, 2, 3, 4] 结果: 10
```

### 3.原理

1.@Profile 条件装配原理

根据前文的积累，我们得知注册bean 的途径大致如下：

| 注册驱动Bean的方式                 | 使用场景          | Bean 注解元信息处理类                       |
| ---------------------------------- | ----------------- | ------------------------------------------- |
| @ComponentScan                     | 对spring 注解扫描 | ClassPathScanningCandidateComponentProvider |
| @Component 或@Configuration        | @Import 导入      | ConfigurationClassPostProcessor             |
| @Bean                              | @Bean 定义        | ConfigurationClassParser                    |
| AnnotationConfigApplicationContext | 注册bean class    | AnnotatedBeanDefinitionReader               |

自 spring 3.0 开始  以上3中注册 bean 的方法均增加了 @Profile  的处理

**ClassPathScanningCandidateComponentProvider #  scanCandidateComponents**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866334278.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866342970.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866349784.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866358937.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866365498.png?raw=true)

**ConfigurationClassParser #  processConfigurationClass**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866522544.png?raw=true)

之后就是同上 的代码

AnnotatedBeanDefinitionReader #  registerBean （实际调用doRegisterBean)  之后就是同上 的代码

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572866615599.png?raw=true)

2.< bean profile="..."  > 原理

较为简单  DefaultBeanDefinitionDocumentReader 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572867165147.png?raw=true)

3.@Conditional 条件装配

spring 4.0以后引入的新特性，前面的@Profile 趋向于“静态激活和配置”，@Conditional  更关注运行时的动态选择

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572919215037.png?raw=true)

仿照 ConditionalOnBean 自定义

4.自定义@Conditional  条件装配

```java
/**
 * 跟据 java 系统属性设置的语言，来加载不同的message
 * @author Ryze
 * @date 2019-11-05 10:10:12
 * @version V1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnConditionalOnSystemProperty.class)
public @interface ConditionalOnSystemProperty {
    /**
     * 名字
     */
    String name();

    /**
     * 值
     */
    String value();
}
/**
 * 实现 比对
 * @author Ryze
 * @date 2019-11-05 10:11
 */
public class OnConditionalOnSystemProperty implements Condition {
    /**
     * 是否匹配
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //全部注解的值
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnSystemProperty.class.getName());
        //单值获取
        String name = (String) attributes.getFirst("name");
        String value = (String) attributes.getFirst("value");
        //获取java 属性的值
        String property = System.getProperty(name);
        //比较是否相等 匹配
        if (Objects.equals(value, property)) {
            return true;
        }
        return false;
    }
}
/**
 * 消息配置
 * @author Ryze
 * @date 2019-11-05 10:22
 */
@Configuration
public class ConditionMessageConfiguration {

    @ConditionalOnSystemProperty(name = "language", value = "Chinese")
    @Bean("message")
    public String chineseLanguage() {
        return "你好，世界";
    }

    @ConditionalOnSystemProperty(name = "language", value = "English")
    @Bean("message")
    public String englishLanguage() {
        return "hello world";
    }
}
/**
 * 启动类
 * @author Ryze
 * @date 2019-11-04 16:01
 */
@ComponentScan(basePackageClasses = ConditionMessageConfiguration.class)
@Configuration
public class ConditionBootStrap {

    public static void main(String[] args) {
        //设置系统变量
        System.setProperty("language", "English");
        //上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //注册 bean
        context.register(ConditionMessageConfiguration.class);
        //启动上下文
        context.refresh();
        //获取 message 的bean
        String message = context.getBean("message", String.class);
        System.out.println(message);
        //关闭上下文
        context.close();
    }
}
```

结果： hello world

3.1~3.2 利用的是Environment # acceptsProfiles(String ..)

自 4.0 之后 Profile 的实现采用的是 Conditional  

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1572921920933.png?raw=true)

总之 条件装配均采用@Conditional 实现，具体原理下面分析

5..@Conditional 条件装配原理

就前面的代码 我们已经看到了

ConditionEvaluator #  shouldSkip（ProfileCondition # matches） 条件装配

# 9.自动装配

spring 不建议修改默认的扫描包范围，因为它将读取所有jar中的类，并且有可能造成某些应用错误。

## 1.理解Spring boot自动装配

从启动类的注解开始 @SpringBootApplication 表示  

@SpringBootConfiguration->@Configuration 

@EnableAutoConfiguration->自动装配模块

@ComponentScan->扫描

启动类并不依赖于  @SpringBootConfiguration  或者  @Configuration  

标注成 EnableAutoConfiguration 一样能启动

@SpringBootConfiguration  并不是必须的，但是有了它 可以省很多写的注解

**自动装配 默认的项，那么怎么不加载不需要的自动装配项呢？**

1.代码配置：

​	@EnableAutoConfiguration.exclude()

​	@EnableAutoConfiguration.excludeName()

2.配置文件配置：

​	spring.autoconfigure.exclude= xxx

实现这个功能，类似于黑名单的方式，要么阻断  @Configuration  的注册，要么使其 @Conditional 不成立

前者实现成本较高，后者对@Configuration class 存在侵入性。

那么怎么实现的呢？下一节

## 2.Spring boot自动装配原理

@EnableAutoConfiguration 开始 注解上标注 

@Import(AutoConfigurationImportSelector.class)  导入 AutoConfigurationImportSelector类，寻找其实现的查找方法

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573011537817.png?raw=true)

为什么会重复？

因为自动配置可能存在组件类名重复定义的情况，那么利用set去重

**getCandidateConfigurations :详情**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573644295379.png?raw=true)

读取EnableAutoConfiguration 实现类

也就是

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573811288152.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573644303329.png?raw=true)



**getExclusions 详情**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573782221129.png?raw=true)

就是获取注解的中排除的项 以及 在配置文件中 配置的项（spring.autoconfigure.exclude）

**checkExcludedClasses详情**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573782412205.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573782572614.png?raw=true)

检查是否合法,当排除的类存在，且不在自动装配的类集合里，那么就报错

**filter详情：**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573796691851.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573791532639.png?raw=true)

也就是读取的 AutoConfigurationImportFilter 的实现类，也就是配置文件中的

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573797049563.png?raw=true)

也就是  

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573797139775.png?raw=true)

主要是match会被调用

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573817891512.png?raw=true)

二分之后

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573817918373.png?raw=true)

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573817932256.png?raw=true)



**过滤的参数autoConfigurationMetadata 怎么来的？**

AutoConfigurationMetadataLoader #loadMetadata 返回 PropertiesAutoConfigurationMetadata

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573797657967.png?raw=true)

这个路径上资源加载

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573805287541.png?raw=true)

整理下总过程：

1.获取 自动装在的 k

2.获取   自动装在的 k/v（候选列表）

3.获取注解排除的 类名 也就是k中的

4.k 中移除 排除的

5.k/v过滤 k 做匹配，也就是看k对应的v 也就是实现类是够存在

6.触发事件

**触发事件详情**：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573822119260.png?raw=true)

也就是 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1573822131633.png?raw=true)

**实现自定义的监听类**

```java
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

```



resources 目录下新建 META-INF 创建 spring.factories 文件

增加内容  ：org.springframework.boot.autoconfigure.AutoConfigurationImportListener=com.example.boothello.listener.MyListener

自定义启动类：

```java
/**
 * 启动类
 * @author Ryze
 * @date 2019-11-04 16:01
 */
@EnableAutoConfiguration(exclude = SpringApplicationAdminJmxAutoConfiguration.class)
public class ConditionBootStrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConditionBootStrap.class)
            .web(WebApplicationType.NONE)
            .run(args)
            .close();
    }
}

```

结果：

自动装配 Class 名单-候选数量:176,实际数量:65,排除数量:1
实际装载class：
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration
org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration
org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration
org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.beans.BeansEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.cloudfoundry.servlet.CloudFoundryActuatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.ReactiveCloudFoundryActuatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.condition.ConditionsReportEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.context.properties.ConfigurationPropertiesReportEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.context.ShutdownEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.elasticsearch.ElasticsearchHealthIndicatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.endpoint.jmx.JmxEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.env.EnvironmentEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.info.InfoContributorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.logging.LogFileWebEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.logging.LoggersEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.management.HeapDumpWebEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.management.ThreadDumpEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.MetricsEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.cache.CacheMetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.jdbc.DataSourcePoolMetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.web.client.RestTemplateMetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.web.reactive.WebFluxMetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.web.servlet.WebMvcMetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration
org.springframework.boot.actuate.autoconfigure.mongo.MongoHealthIndicatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.redis.RedisHealthIndicatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.scheduling.ScheduledTasksEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthIndicatorAutoConfiguration
org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration
org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.web.mappings.MappingsEndpointAutoConfiguration
org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration
org.springframework.boot.actuate.autoconfigure.web.servlet.ServletManagementContextAutoConfiguration
排除class：
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration

**@EnableAutoConfiguration 的生命周期**

该注解的Import AutoConfigurationImportSelector.class  

而 AutoConfigurationImportSelector  implements DeferredImportSelector  

字面意思 DeferredImportSelector  就是延迟选择导入，当所有的 @Configuration 处理完之后 才运作，可通过

实现Ordered接口调整优先执行顺序，AutoConfigurationImportSelector  其优先级最低

（Ordered.LOWEST_PRECEDENCE - 1）

而 AutoConfigurationImportSelector # selectImports 是在  ConfigurationClassParser # processImports 中执行，往

deferredImportSelectors队列放值。

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574057414456.png?raw=true)

然后 在 ConfigurationClassParser # parse 中 processDeferredImportSelectors的执行

也就是，DeferredImportSelectorHolder 队列在  ConfigurationClassParser #processDeferredImportSelectors 中执行

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574045827648.png?raw=true)

具体代码：（先排序）（最后倒入  会递归处理同样导入的需要导入的）

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574155560338.png?raw=true)

获取导入集合的实现：

默认（未指定）是DeferredImportSelectorGrouping 

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574155591843.png?raw=true)

而自动导入的 应该是 AutoConfigurationGroup

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574230528943.png?raw=true)

**具体的排序：**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574230544500.png?raw=true)

**真正的排序：**

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574230564837.png?raw=true)

以上 就是   **@EnableAutoConfiguration 排序自动装配组件**

AutoConfigurationSorter类排序 处理的  @AutoConfigureAfter @AutoConfigureBefore  @AutoConfigureOrder 注解

获得排序。

**@EnableAutoConfiguration 自动装配的Basepackages**

@EnableAutoConfiguration上的 @AutoConfigurationPackage

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574233243394.png?raw=true)

查看该注解

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574233624274.png?raw=true)

类似于import的类 实现

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574233653903.png?raw=true)

该方式 会被利用来做 包扫描 例如JPA 等

## 3.自定义Spring boot自动装配

**1.自动装配class命名潜规则**

xxxAutoConfiguration

**2.自动装配package命名潜规则**

${ root-package }

|-autoconfigure

​	|-${module-package }

​		|-xxxAutoConfiguration

​		|-${ sub-module-package}

​			|-...

**3.starter 名**

${ module}-spring-boot-starter   （第三方）

spring-boot-starter-${ module}   （官方）

**4.自定义**

定义一个formatter的 starter（格式化的）

新建项目 springboot 项目   formatter-spring-boot-starter

新建包名：autoconfigure.formatter

新建格式化的接口以及默认实现

```java
/**
 * 格式化
 * @author Ryze
 * @date 2019-11-20 16:46:01
 * @version V1.0.0
 */
public interface Formatter {

    String formatter(Object object);
}
/**
 * 默认实现
 * @author Ryze
 * @date 2019-11-20 16:46
 */
public class DefaultFormatter implements Formatter {
    @Override
    public String formatter(Object object) {
        return String.valueOf(object);
    }
}
```

然后写 自动装配类

```java
/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 */
@Configuration
public class FormatterAutoConfiguration {
    @Bean
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }
}

```

在目录resource 下新建文件夹META-INF  新建文件 spring.factories

然后配置上 这个装配类 

```xml
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\  com.example.formatterspringbootstarter.autoconfigure.formatter.FormatterAutoConfiguration
```

pom.xml 详情：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>formatter-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>formatter-spring-boot-starter</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

切换到 项目目录下 执行  mvn -Dmaven.test.skip  -U clean install 

然后在测试项目中 引入依赖

编写启动测试类

```java
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
```

执行结果：

{测试=格式化}

至此 完成了最简单的 自定义 starter模块，之后会更加丰富

## 4.Spring boot条件化自动装配

即@Conditional 与@Configuration 组合使用，在AutoConfigurationImportFilter 的实现类 OnClassCondition 中过滤非法自动装配的Class,间接的接触条件注解 ConditionalOnClass

**1.Class条件注解**  @ConditionalOnClass  @ConditionalOnMissingClass

均使用@Conditional 实现，OnClassCondition 具体实现：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574245909729.png?raw=true)

那么利用这些修改starter，String的格式化满足不了 对象的格式化，那么引入jackson

增加 依赖

```xml
        <!-- Jackson 依赖 -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <optional>true</optional>
        </dependency>
```

增加json 格式化

```java
/**
 * json 格式化
 * @author Ryze
 * @date 2019-11-20 18:43
 */
public class JsonFormatter implements Formatter {
    private final ObjectMapper objectMapper;

    public JsonFormatter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String formatter(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

```

修改装配类

```java
/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 */
@Configuration
public class FormatterAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass(value = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter jsonFormatter() {
        return new JsonFormatter();
    }
}

```

重新执行 mvn clean install

修改测试项目的启动类

```java

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
        System.out.printf("实现类 %s,格式化结果%s", bean.getClass().getSimpleName(), formatter);
        System.out.println();
        run.close();
    }
}

```

执行：

实现类 DefaultFormatter,格式化结果{测试=格式化}

在测试项目中引入依赖

```xml
    <!-- Jackson 依赖 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
```
重新执行：

实现类 JsonFormatter,格式化结果{"测试":"格式化"}

实现了格式化组件，切换的自动装配

实际上 jackson 也是这么装配来的

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574248110523.png?raw=true)

那么装配的bean 是否可以复用呢  ？肯定是可以的，下面接着看

**2 Bean的条件注解**  @ConditionalOnBean   @ConditionalOnMissingBean

与 上面区别就是 bean的条件注解匹配的是 BeanFactory中的Bean类型和名字，class的条件注解匹配的是上下文中已处理的BeanDefinition  

实现是OnBeanCondition：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574306151470.png?raw=true)

以上就是对这几个注解的处理，其实大致过程很相似，先构建bean 的表达式，BeanSearchSpec 是对 注解和bean 的包装

（SearchStrategy 匹配的范围，当前/祖先/全部）@ConditionalOnBean 、@ConditionalOnSingleCandidate 、

@ConditionalOnMissingBean搜索策略  都是  SearchStrategy.ALL（全部）

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574323088186.png?raw=true)

然后归结于getMatchingBeans 方法 获取匹配的bean

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574324810348.png?raw=true)

获取忽略的bean 和 根据注解type、value的是现实一样的，如下

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574324830394.png?raw=true)

先获取BeanTypeRegistry 后调用 getNamesForType 获取结果，详情：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574331303002.png?raw=true)

也就是

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574331328565.png?raw=true)

最后调用 ：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574331349876.png?raw=true)

也就是先从默认的bean工厂获取单例bean ，没有的话才做其他处理，我们回到 OnBeanCondition类，其实现了 SpringBootCondition

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574331505578.png?raw=true)

排除bean的注册，也就是说 标注了@ConditionalOnBean @ConditionalOnSingleCandidate 

@ConditionalOnMissingBean的类，并不会被着急创建成bean，在注册成bean 的时候会被 ConditionEvaluator # 

shouldSkip  跳过，getSingleton 返回null ，然后从bean的定义中寻找。这就是为啥，@ConditionalOnBean

@ConditionalOnMissingBean 强烈要求仅在自动装配中使用，主要是其基于bean的定义的名称、类型匹配的

下面是对 annotation的处理

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574324909193.png?raw=true)

最终返回匹配的结果

根据 以上的@ConditionalOnBean  @ConditionalOnMissingBean 的实现，修改starter的实现

也就是引入jackson的bean 那么 查看jackson的自动装配

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574336544103.png?raw=true)

修改starter：

修改json的构造

```java
/**
 * json 格式化
 * @author Ryze
 * @date 2019-11-20 18:43
 */
public class JsonFormatter implements Formatter {
    private final ObjectMapper objectMapper;

    public JsonFormatter() {
        this(new ObjectMapper());
    }

    public JsonFormatter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String formatter(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

```

修改自动装配

```java
/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 * @AutoConfigureAfter(value = JacksonAutoConfiguration.class) 是因为装载顺序的原因
 */
@Configuration
@AutoConfigureAfter(value = JacksonAutoConfiguration.class)
public class FormatterAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass(value = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    @ConditionalOnMissingBean(type = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter jsonFormatter() {
        return new JsonFormatter();
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public Formatter ObjectMapperFormatter(ObjectMapper objectMapper) {
        return new JsonFormatter(objectMapper);
    }
}
```

然后重新 mvn clean install 

在 测试项目引入依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

修改启动类：

```java
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
        Map<String, Formatter> beansOfType = run.getBeansOfType(Formatter.class);
       if (CollectionUtils.isEmpty(beansOfType)) {
            throw new IllegalArgumentException("未找到匹配类型");
        }
        beansOfType.forEach((k, v) -> System.out.printf("实现类 %s,名字 %s,格式化结果%s", v.getClass().getSimpleName(), k, v.formatter(map)));
        System.out.println();
        run.close();
    }
}
```

运行结果：

实现类 JsonFormatter,名字 ObjectMapperFormatter,格式化结果{"测试":"格式化"}

也就是说 引入spring-boot-starter-web 实际上相当于 满足 jackson bean的 初始化，其存在那么会被自动装配

**属性条件注解**

@ConditionalOnProperty，扩展于Spring Enviroment

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574393651488.png?raw=true)

调整starter

```java

/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 * 匹配formatter.enable的值 是否为true
 */
@Configuration
@AutoConfigureAfter(value = JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "formatter", name = "enable", havingValue = "true")
public class FormatterAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass(value = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    @ConditionalOnMissingBean(type = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter jsonFormatter() {
        return new JsonFormatter();
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public Formatter ObjectMapperFormatter(ObjectMapper objectMapper) {
        return new JsonFormatter(objectMapper);
    }
}
```

重新安装starter依赖

测试项目运行得到：

Exception in thread "main" java.lang.IllegalArgumentException: 未找到匹配类型
	at com.example.boothello.starterTest1.FormatterBootStrap.main(FormatterBootStrap.java:33)

只有 formatter.enable=true 才会被装载

修改测试项目的启动类

```java
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
            .properties("formatter.enable=true")
            .run(args);

        Map<String, Object> map = new HashMap<>();
        map.put("测试", "格式化");
        Map<String, Formatter> beansOfType = run.getBeansOfType(Formatter.class);
        if (CollectionUtils.isEmpty(beansOfType)) {
            throw new IllegalArgumentException("未找到匹配类型");
        }
        beansOfType.forEach((k, v) -> System.out.printf("实现类 %s,名字 %s,格式化结果%s", v.getClass().getSimpleName(), k, v.formatter(map)));
        System.out.println();
        run.close();
    }
}
```

运行得到：

实现类 JsonFormatter,名字 ObjectMapperFormatter,格式化结果{"测试":"格式化"}

在测试项目的配置文件写上formatter.enable=false

得到结果：

Exception in thread "main" java.lang.IllegalArgumentException: 未找到匹配类型
	at com.example.boothello.starterTest1.FormatterBootStrap.main(FormatterBootStrap.java:33)

也就是说  配置文件的值会覆盖代码中设置的值

**默认不写的话 就会报错，所以我们需要设置**

ConditionalOnProperty#matchIfMissing为true  也就是 不写的话 也是匹配的

@ConditionalOnProperty(prefix = "formatter", name = "enable", havingValue = "true",matchIfMissing =true )

重新安装starter依赖

运行得到：

实现类 JsonFormatter,名字 ObjectMapperFormatter,格式化结果{"测试":"格式化"}



就像是 spring的风格，我们默认使用什么什么，不用的话 可以用什么禁止掉

**Resource条件注解**

@ConditionalOnResource

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574407391761.png?raw=true)

只有resources 资源存在才匹配，实现是在 OnResourceCondition 类

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574412560210.png?raw=true)

那么，资源加载器是 什么呢？

这个方法会被在 ConditionEvaluator #shouldSkip 方法中调用，其context 在其初始化时调用

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574412728581.png?raw=true)

调用这个构造的地方有5处：

1.AnnotatedBeanDefinitionReader 的构造 传递的资源加载器是null

2.AnnotatedBeanDefinitionReader # setEnvironment  传递的资源加载器是null

3.ClassPathScanningCandidateComponentProvider # isConditionMatch 最终是在 setResourceLoader 中初始化，（其 实现了 ResourceLoaderAware 作为spring bean 时 这个字段会被上下文填充 ）但是不  能确定其状态 ，存在变数

4.ConfigurationClassBeanDefinitionReader 的构造 继续跟踪到 ConfigurationClassPostProcessor # processConfigBeanDefinitions 最终到setResourceLoader 始化，（其 实现了 ResourceLoaderAware 作为spring bean 时 这个字段会被上下文填充 ）但是不  能确定其状态 ，存在变数

5.ConfigurationClassParser的构造 被 ConfigurationClassPostProcessor# processConfigBeanDefinitions 调用，同上了

因此资源加载器 1. ResourceLoaderAware  的回调（spring 上下文）

​						   2.null （默认的 DefaultResourceLoader）

ResourceLoaderAware 的 setResourceLoader  会被 ApplicationContextAwareProcessor 调用：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574414308589.png?raw=true)

其传递的上下文是在构造中初始化：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574414347331.png?raw=true)

ConfigurableApplicationContext 是 ResourceLoader 的子类

其初始化是在  AbstractApplicationContext #  prepareBeanFactory

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574414496389.png?raw=true)

被 AbstractApplicationContext  # refresh 调用，上下文启动过程中，那么传递的是当前spring应用上下文实例

资源加载 ResourceLoader 的类图：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574418630888.png?raw=true)

ResourcePatternResolver 是 ResourceLoader 的扩展接口，其实现类 PathMatchingResourcePatternResolver：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574419456962.png?raw=true)

关联到 AbstractApplicationContext （DefaultResourceLoader的实现类）：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574419468349.png?raw=true)

AbstractApplicationContext 实现了 ConfigurableApplicationContext 并继承了 DefaultResourceLoader，并未覆盖

DefaultResourceLoader# getResource  也就是说默认情况下 其实现是 DefaultResourceLoader，默认的

Resourceloader 就是  DefaultResourceLoader

其实现获取资源的方法是：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574423413975.png?raw=true)

其中   ProtocolResolver 是spring 扩展的协议解析

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574423431857.png?raw=true)

具体定义如下：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574423492742.png?raw=true)

那么 我们整合到starter中

```java
/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 */
@Configuration
@AutoConfigureAfter(value = JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "formatter", name = "enable", havingValue = "true",matchIfMissing =true )
@ConditionalOnResource(resources = "1.properties") //只有 1.properties 存在才会加载装配
public class FormatterAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass(value = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    @ConditionalOnMissingBean(type = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter jsonFormatter() {
        return new JsonFormatter();
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public Formatter ObjectMapperFormatter(ObjectMapper objectMapper) {
        return new JsonFormatter(objectMapper);
    }
}
```

starter重新 intall

在测试项目 resource新建文件1.properties

运行结果：

实现类 JsonFormatter,名字 ObjectMapperFormatter,格式化结果{"测试":"格式化"}

**Web 应用的条件注解**  @ConditionalOnWebApplication  @ConditionalOnNotWebApplication

是否在 web环境下，并引入详细的type限制

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574424764192.png?raw=true)

实现是在 OnWebApplicationCondition ，匹配为：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574425842347.png?raw=true)

具体的是现实：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574425864603.png?raw=true)

整合starter

在FormatterAutoConfiguration 添加注解 @ConditionalOnNotWebApplication 也就是只有在非 web环境才会执行装配

重新inatall

运行测试项目 结果：

实现类 JsonFormatter,名字 ObjectMapperFormatter,格式化结果{"测试":"格式化"}

**Spring表达式条件注解**

@ConditionalOnExpression详情：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574650363149.png?raw=true)

其实现类：

![image](https://github.com/RyzeUserName/spring-boot/blob/master/assets/1574650379375.png?raw=true)

其实现较为简单，一般用于类似于，一个boolean 的值

修改starter

在自动装配的类上增加 @ConditionalOnExpression("${formatter.flag:true}")

```java
/**
 * 格式化的装配
 * @author Ryze
 * @date 2019-11-20 16:48
 */
@Configuration
@AutoConfigureAfter(value = JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "formatter", name = "enable", havingValue = "true",matchIfMissing =true )
@ConditionalOnResource(resources = "1.properties")
@ConditionalOnNotWebApplication
@ConditionalOnExpression("${formatter.flag:true}")
public class FormatterAutoConfiguration {
    @Bean
    @ConditionalOnMissingClass(value = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter defaultFormatter() {
        return new DefaultFormatter();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    @ConditionalOnMissingBean(type = "com.fasterxml.jackson.databind.ObjectMapper")
    public Formatter jsonFormatter() {
        return new JsonFormatter();
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public Formatter ObjectMapperFormatter(ObjectMapper objectMapper) {
        return new JsonFormatter(objectMapper);
    }
}

```

install starter

运行测试类：

实现类 JsonFormatter,名字 ObjectMapperFormatter,格式化结果{"测试":"格式化"}

配置文件增加：formatter.flag=false

重新运行：

Exception in thread "main" java.lang.IllegalArgumentException: 未找到匹配类型
	at com.example.boothello.starterTest1.FormatterBootStrap.main(FormatterBootStrap.java:34)

# 10.初始化

## 1.构造阶段

## 2.配置阶段

# 11.运行

## 1.准备

## 2.上下文启动阶段

## 3.上下文启动后阶段

# 12.结束

## 1.正常结束

## 2.异常结束

# 13.退出

## 1.正常退出

## 2.异常退出


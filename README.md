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

# 4.嵌入式容器

# 5.自动装配

# 6.Production-Ready

# 7.注解

# 8.注解驱动设计模式

# 9.自动装配

# 10.初始化

# 11.运行

# 12.结束

# 13.退出


package com.example.boothello.enableTest1;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * ⑤
 * 实现选择器
 * @author Ryze
 * @date 2019-10-29 14:30
 */
public class ServerImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //@EnableServer 获取注解值
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableServer.class.getName());
        //获取 注解 type的值
        Server.Type type = (Server.Type) annotationAttributes.get("type");
        String[] importClazz = new String[0];
        switch (type) {
            case HTTP:
                importClazz = new String[]{HttpServer.class.getName()};
                break;
            case FTP:
                importClazz = new String[]{FtpServer.class.getName()};
                break;
        }
        return importClazz;
    }
}

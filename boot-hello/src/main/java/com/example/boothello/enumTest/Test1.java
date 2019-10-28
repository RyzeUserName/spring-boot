package com.example.boothello.enumTest;

import com.example.boothello.enumTest2.MyAnnotion;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.Set;

/**
 *测试　注解
 * @author Ryze
 * @date 2019-10-22 11:17
 */
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

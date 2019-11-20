//package com.example.boothello.enumTest2;
//
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.core.type.StandardAnnotationMetadata;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.CollectionUtils;
//
//import java.util.LinkedHashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * spring  实现获取注解 值
// * @author Ryze
// * @date 2019-10-24 15:31
// */
//@MyAnnotion
//public class Test4 {
//    public static void main(String[] args) {
//        AnnotationMetadata metadata = new StandardAnnotationMetadata(Test4.class);
//        DealAnnotion(metadata);
//    }
//
//    public static void DealAnnotion(AnnotationMetadata metadata) {
//        Set<String> collect = metadata.getAnnotationTypes().stream().map(metadata::getMetaAnnotationTypes).collect(LinkedHashSet::new, Set::addAll, Set::addAll);
//        collect.stream().forEach(m -> {
//            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(m);
//            if (!CollectionUtils.isEmpty(annotationAttributes)) {
//                annotationAttributes.forEach((k, v) ->
//                    System.out.println("注解 " + ClassUtils.getShortName(m) + "属性" + k + " = " + v));
//            }
//        });
//    }
//}

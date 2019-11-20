//package com.example.boothello.enumTest2;
//
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.annotation.Target;
//import java.lang.reflect.AnnotatedElement;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * 获取全部注解 和 值
// * @author Ryze
// * @date 2019-10-24 14:04
// */
//@MyAnnotion
//public class Test3 {
//    public static void main(String[] args) {
//        AnnotatedElement annotatedElement = Test3.class;
//        MyAnnotion annotation = annotatedElement.getAnnotation(MyAnnotion.class);
//        Set<Annotation> allAnnotions = getAllAnnotions(annotation);
//        allAnnotions.forEach(Test3::printAnnotions);
//    }
//
//    static Set<Annotation> getAllAnnotions(Annotation annotation) {
//        Annotation[] annotations = annotation.annotationType().getAnnotations();
//        if (ObjectUtils.isEmpty(annotations)) {
//            return Collections.emptySet();
//        }
//        //过滤掉  java.lang.annotation 包下的基础注解
//        Set<Annotation> collect = Stream.of(annotations).filter(
//            a -> !Target.class.getPackage().equals(a.annotationType().getPackage())
//        ).collect(Collectors.toSet());
//        //递归
//        Set<Annotation> collect1 = collect.stream().map(Test3::getAllAnnotions).collect(HashSet::new, Set::addAll, Set::addAll);
//        //加起来
//        collect.addAll(collect1);
//        return collect;
//    }
//
//    static void printAnnotions(Annotation annotation) {
//        Class<? extends Annotation> aClass = annotation.annotationType();
//        ReflectionUtils.doWithMethods(aClass,
//            method -> System.out.println("注解-->" + aClass.getSimpleName() + "方法名 -->" + method.getName() + "  值-->" + ReflectionUtils.invokeMethod(method, annotation)),
//            method -> !method.getDeclaringClass().equals(Annotation.class));
//    }
//}

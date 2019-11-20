//package com.example.boothello.enumTest3;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionException;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.support.SimpleTransactionStatus;
//
//import java.util.Map;
//
///**
// * 启动类
// * @author Ryze
// * @date 2019-10-28 18:10
// */
//@ComponentScan(basePackageClasses = TxService.class)
//@EnableTransactionManagement
//public class TxBootStrap {
//    public static void main(String[] args) {
//        //注册当前引导类
//        AnnotationConfigApplicationContext bootStrap = new AnnotationConfigApplicationContext(TxBootStrap.class);
//        //获取 TxService 的bean
//        Map<String, TxService> beansOfType = bootStrap.getBeansOfType(TxService.class);
//        beansOfType.forEach((name, txService) -> {
//            System.out.printf("Bean 名称 : %s,对象: %s\n", name, txService);
//            txService.save();
//        });
//    }
//
//    @Bean("txManager")
//    public PlatformTransactionManager txManager() {
//        return new PlatformTransactionManager() {
//            @Override
//            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
//                return new SimpleTransactionStatus();
//            }
//
//            @Override
//            public void commit(TransactionStatus status) throws TransactionException {
//                System.out.println("txManager： 事务提交");
//            }
//
//            @Override
//            public void rollback(TransactionStatus status) throws TransactionException {
//                System.out.println("txManager： 事务回滚");
//            }
//        };
//    }
//
//    @Bean("txManager2")
//    public PlatformTransactionManager txManager2() {
//        return new PlatformTransactionManager() {
//            @Override
//            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
//                return new SimpleTransactionStatus();
//            }
//
//            @Override
//            public void commit(TransactionStatus status) throws TransactionException {
//                System.out.println("txManager2： 事务提交");
//            }
//
//            @Override
//            public void rollback(TransactionStatus status) throws TransactionException {
//                System.out.println("txManager2： 事务回滚");
//            }
//        };
//    }
//}

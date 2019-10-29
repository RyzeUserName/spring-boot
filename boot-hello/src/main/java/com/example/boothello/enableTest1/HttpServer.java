package com.example.boothello.enableTest1;

import org.springframework.stereotype.Component;

/**
 * ②
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

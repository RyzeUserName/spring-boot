package com.example.boothello.enableTest1;

import org.springframework.stereotype.Component;

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

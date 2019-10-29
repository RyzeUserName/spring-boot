package com.example.boothello.enableTest1;

/**
 * ①
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

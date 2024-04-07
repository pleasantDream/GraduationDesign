package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TZH
 */
@SpringBootApplication
public class backgroundApplication {
    public static void main( String[] args ){
//        // 设置代理
////        String proxy = "192.168.241.1";
//        String proxy = "127.0.0.1";
//        // 翻墙软件代理的端口
//        int port = 7890;
//        System.setProperty("proxyType", "4");
//        System.setProperty("proxyPort", Integer.toString(port));
//        System.setProperty("proxyHost", proxy);
//        System.setProperty("proxySet", "true");
        SpringApplication.run(backgroundApplication.class, args);
    }
}

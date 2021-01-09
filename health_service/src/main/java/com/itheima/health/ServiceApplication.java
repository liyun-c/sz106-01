package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * main方法为zookeeper提供服务
 *
 * @author liyun
 * @date 2021/1/6 17:23
 */

public class ServiceApplication {
    public static void main(String[] args) throws IOException {
        //读取配置文件 创建核心容器 提交服务
        new ClassPathXmlApplicationContext("classpath:spring-service.xml");
        System.in.read();
    }
}

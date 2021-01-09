package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 开启定时任务 的调度容器  并注册提供服务到zookeeper上
 *
 * @author liyun
 * @date 2021/1/9 19:40
 */


public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:spring-jobs.xml");
        System.in.read();
    }
}

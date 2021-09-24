package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lisalou
 * @create 2021-09-23-2:24 下午
 */
@SpringBootApplication
@EnableFeignClients
public class OrderMain84 {
    public static void main(String[] args) {
        System.out.println("hotfix");
        SpringApplication.run(OrderMain84.class,args);
    }
}

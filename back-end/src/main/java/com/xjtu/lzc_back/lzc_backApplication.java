package com.xjtu.lzc_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@MapperScan("com.xjtu.lzc_back.mapper")
@EnableTransactionManagement
public class lzc_backApplication {
    public static void main(String[] args) {
        SpringApplication.run(lzc_backApplication.class, args);//111
    }
}


package com.alex;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.alex.mapper")
@SpringBootApplication(scanBasePackages = {"com.alex"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

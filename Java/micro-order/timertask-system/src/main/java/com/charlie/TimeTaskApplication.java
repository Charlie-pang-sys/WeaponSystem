package com.charlie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.charlie.dao")
@EnableScheduling
@SpringBootApplication
public class TimeTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimeTaskApplication.class, args);
    }
}

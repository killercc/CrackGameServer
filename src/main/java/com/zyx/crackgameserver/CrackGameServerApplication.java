package com.zyx.crackgameserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan("com.zyx.crackgameserver")
public class CrackGameServerApplication{


    public static void main(String[] args) {
        SpringApplication.run(CrackGameServerApplication.class, args);
    }


}

package com.qjn.xiangxi_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.qjn.xiangxi_system.mapper")
public class XiangxiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiangxiSystemApplication.class, args);
    }

}

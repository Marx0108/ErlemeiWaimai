package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
//可以使用日志
@ServletComponentScan
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ReggieApplication {
    public static void main(String[] args) {
//注意区别        SpringBootApplication.run(ReggieApplication.class,args);
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功");
    }
}

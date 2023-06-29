package com.erlemei.waimai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
public class ErlemeiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErlemeiApplication.class, args);
        log.info("项目启动成功");

    }
}

package com.ztu.cloud.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Jager
 * @description 启动
 * @date 2020/06/21-11:49
 **/
@SpringBootApplication
public class CloudApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

}

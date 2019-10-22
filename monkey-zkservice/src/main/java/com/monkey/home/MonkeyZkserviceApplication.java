package com.monkey.home;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * zkService启动类
 * @author yangfan
 * @createTime 2019-10-18 12:30:53
 */
@SpringBootApplication
@EnableDubboConfiguration
public class MonkeyZkserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyZkserviceApplication.class, args);
    }

}

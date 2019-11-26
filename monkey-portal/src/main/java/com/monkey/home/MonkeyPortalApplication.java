package com.monkey.home;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 门户项目启动入口
 *
 * @author yangfan
 * @createTime 2019-10-19 09:39:48
 */
@SpringBootApplication
@EnableDubboConfiguration
public class MonkeyPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyPortalApplication.class, args);
    }

}

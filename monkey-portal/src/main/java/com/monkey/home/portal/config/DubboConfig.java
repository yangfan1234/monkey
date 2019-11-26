package com.monkey.home.portal.config;

import com.alibaba.dubbo.config.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dubbo配置类
 * @author yangfan
 * @createTime 2019-11-24 13:13
 */
@Configuration
public class DubboConfig {

    /**
     * 如果dubbo生产端未启动，消费段启动时会报错，需配置
     * @author yangfan
     * @createTime 2019-11-24 01:15:44
     */
    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setCheck(false);
        config.setTimeout(30000);
        return config;
    }
}

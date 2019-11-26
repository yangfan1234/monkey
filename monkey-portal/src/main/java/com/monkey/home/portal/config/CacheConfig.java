package com.monkey.home.portal.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 * @author yangfan
 * @createTime 2019-11-26 22:24
 */
@Configuration
public class CacheConfig {

    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.1.117:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}

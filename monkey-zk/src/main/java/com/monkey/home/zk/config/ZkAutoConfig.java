package com.monkey.home.zk.config;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * zk自动配置
 *
 * @author yangfan
 * @createTime 2019-10-18 23:08
 */
@Configuration
public class ZkAutoConfig {

    @Autowired
    private ZkConfigParam config;

    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        return new ZooKeeper(config.getConn(), config.getTimeout(), event -> {});
    }

    @Bean
    public ZkClient zkClient() {
        return new ZkClient(config.getConn(), config.getTimeout());
    }

}

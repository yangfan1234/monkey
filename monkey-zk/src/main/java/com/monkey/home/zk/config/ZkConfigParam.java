package com.monkey.home.zk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zk配置对象
 * @author yangfan
 * @createTime 2019-10-17 23:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "monkey.home.zk", ignoreInvalidFields = true)
public class ZkConfigParam {

    /** zk连接信息 */
    private String conn = "localhost:2181";

    /** 超时时间 */
    private int timeout = 20000;

    /** 项目注册节点名称 */
    private String registryName;

    /** 项目注册节点储存的数据 */
    private String registryData;

    /** 自动注册 */
    private boolean autoRegistry = true;
}

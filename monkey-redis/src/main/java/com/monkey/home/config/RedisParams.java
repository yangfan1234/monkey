package com.monkey.home.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置参数
 * @author yangfan
 * @createTime 2019-11-21 21:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "monkey.home.redis")
public class RedisParams {

    /** 模式-单节点或者主从 */
    public static final String REDIS_MODE_SINGLE = "single";
    /** 模式-哨兵 */
    public static final String REDIS_MODE_SENTINEL = "sentinel";
    /** 模式-集群 */
    public static final String REDIS_MODE_CLUSTER = "cluster";
    
    /** 集群模式single-单机或主从，sentinel-哨兵，cluster-集群 */
    private String mode;
    
    /** 最大连接数 */
    private Integer maxTotal = 20;

    /** 最大空闲数 */
    private Integer maxIdle = 10;

    /** 最小空闲数 */
    private Integer minIdle = 10;
    
    /** ip地址 */
    private String singleAddr = "127.0.0.1:6379";
    
    /** 连接超时时间 */
    private Integer connTimeout = 3000;
    
    /** 连接响应时间 */
    private Integer soTimeout = 3000;
    
    /** 哨兵模式时主节点名称 */
    private String masterName = "mymaster";
    
    /** 如果是哨兵或者集群时的节点地址配置，多个用“,”分割 */
    private String nodes;
}

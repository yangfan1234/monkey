package com.monkey.home.config;

import com.monkey.home.support.MonkeyJedisTemplate;
import com.monkey.home.support.RedisModeClusterCondition;
import com.monkey.home.support.RedisModeSentinelCondition;
import com.monkey.home.support.RedisModeSigleCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * redis自动配置类，本配置是单独使用Jedis的情况下的配置
 * 根据redis部署模式条件化配置不同的连接对象
 * @author yangfan
 * @createTime 2019-11-21 21:38
 */
@Configuration
public class RedisAutoConfig {

    @Autowired
    private RedisParams redisParams;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisParams.getMaxTotal());
        poolConfig.setMaxIdle(redisParams.getMaxIdle());
        poolConfig.setMinIdle(redisParams.getMinIdle());
        return poolConfig;
    }

    /**
     * 单节点
     * @author yangfan
     * @createTime 2019-11-24 10:03:08
     * @param poolConfig 连接池配置对象
     */
    @Bean
    @Conditional(value = RedisModeSigleCondition.class)
    public JedisPool jedisPool(JedisPoolConfig poolConfig) {
        String singleAddr = redisParams.getSingleAddr();
        JedisPool pool = null;
        if (StringUtils.isNotBlank(singleAddr)) {
            String[] addrs = singleAddr.split(":");
            pool = new JedisPool(poolConfig, addrs[0], Integer.parseInt(addrs[1]), redisParams.getConnTimeout(), null);
        } else {
            pool = new JedisPool(poolConfig);
        }
        return pool;
    }

    /**
     * 哨兵
     * @author yangfan
     * @createTime 2019-11-24 10:04:39
     * @param poolConfig 连接池配置对象
     * @return redis.clients.jedis.JedisSentinelPool
     */
    @Bean
    @Conditional(value = RedisModeSentinelCondition.class)
    public JedisSentinelPool jedisSentinelPool(JedisPoolConfig poolConfig) {
        Set<String> sentinels = new HashSet<>();
        String nodes = redisParams.getNodes();
        String[] nodesAddr = nodes.split(",");
        for (String s : nodesAddr) {
            String[] node = s.split(":");
            sentinels.add(new HostAndPort(node[0], Integer.parseInt(node[1])).toString());
        }
        return new JedisSentinelPool(redisParams.getMasterName(), sentinels,
                poolConfig, redisParams.getConnTimeout(), null);
    }

    /**
     * 集群
     * @author yangfan
     * @createTime 2019-11-24 10:04:39
     * @param poolConfig 连接池配置对象
     * @return redis.clients.jedis.JedisSentinelPool
     */
    @Bean
    @Conditional(value = RedisModeClusterCondition.class)
    public JedisCluster jedisCluster(JedisPoolConfig poolConfig) {
        Set<HostAndPort> sentinels = new HashSet<>();
        String nodes = redisParams.getNodes();
        String[] nodesAddr = nodes.split(",");
        for (String s : nodesAddr) {
            String[] node = s.split(":");
            sentinels.add(new HostAndPort(node[0], Integer.parseInt(node[1])));
        }
        return new JedisCluster(sentinels, poolConfig);
    }

    @Bean
    public MonkeyJedisTemplate monkeyJedisTemplate() {
        MonkeyJedisTemplate jedisTemplate = null;
        if (RedisParams.REDIS_MODE_SINGLE.equals(redisParams.getMode())) {
            jedisTemplate = new MonkeyJedisTemplate(jedisPool(jedisPoolConfig()));
        }
        if (RedisParams.REDIS_MODE_SENTINEL.equals(redisParams.getMode())) {
            jedisTemplate = new MonkeyJedisTemplate(jedisSentinelPool(jedisPoolConfig()));
        }
        if (RedisParams.REDIS_MODE_CLUSTER.equals(redisParams.getMode())) {
            jedisTemplate = new MonkeyJedisTemplate(jedisCluster(jedisPoolConfig()));
        }
        return jedisTemplate;
    }

}

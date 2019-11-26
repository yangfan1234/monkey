package com.monkey.home.support;

import com.monkey.home.config.RedisParams;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

/**
 * redis客户端使用模板抽象类
 * @author yangfan
 * @createTime 2019-11-24 10:49
 */
public class MonkeyJedisTemplate {

    private JedisPool jedisPool;

    private JedisSentinelPool sentinelPool;

    private JedisCluster cluster;

    /** redis使用模式 */
    private String mode;

    public MonkeyJedisTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        mode = RedisParams.REDIS_MODE_SINGLE;
    }

    public MonkeyJedisTemplate(JedisSentinelPool sentinelPool) {
        this.sentinelPool = sentinelPool;
        mode = RedisParams.REDIS_MODE_SENTINEL;
    }

    public MonkeyJedisTemplate(JedisCluster cluster) {
        this.cluster = cluster;
        mode = RedisParams.REDIS_MODE_CLUSTER;
    }

    /**
     * 执行单节点、主从、哨兵模式的操作
     * @author yangfan
     * @createTime 2019-11-24 11:06:42
     * @param action 业务操作
     */
    public Object excuteCommon(CommonAction action) {
        Jedis jedis = null;
        try {
            if (RedisParams.REDIS_MODE_SINGLE.equals(mode)) {
                jedis = jedisPool.getResource();
            }

            if (RedisParams.REDIS_MODE_SENTINEL.equals(mode)) {
                jedis = sentinelPool.getResource();
            }
            return action.doBusiness(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 执行集群模式的操作,注意集成到spring后不能主动调用cluster.close() 方法，否则下次调用报错
     * @author yangfan
     * @createTime 2019-11-24 11:06:42
     * @param action 业务操作
     */
    public Object excuteCluster(ClusterAction action) {
        try {
            return action.doBusiness(cluster);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Jedis getJedisInstence() {
        Jedis jedis = null;
        try {
            if (RedisParams.REDIS_MODE_SINGLE.equals(mode)) {
                jedis = jedisPool.getResource();
            }

            if (RedisParams.REDIS_MODE_SENTINEL.equals(mode)) {
                jedis = sentinelPool.getResource();
            }
            return jedis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单节点、主从、哨兵模式的业务封装接口
     * @author yangfan
     * @createTime 2019-11-24 10:58:32
     */
    public interface CommonAction {
        Object doBusiness(Jedis jedis);
    }

    /**
     * 集群模式的业务封装接口
     * @author yangfan
     * @createTime 2019-11-24 10:58:32
     */
    public interface ClusterAction {
        Object doBusiness(JedisCluster jedis);
    }


}

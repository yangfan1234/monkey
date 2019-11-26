package com.monkey.home.portal.prctice;

import com.monkey.home.support.MonkeyJedisTemplate;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;*/
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Pipeline;
import java.util.Arrays;

/**
 * 练习使用
 * @author yangfan
 * @createTime 2019-11-24 12:32
 */
@RestController
@RequestMapping("practice")
public class MonkeyPracticeController {

    @Autowired
    private MonkeyJedisTemplate jedisTemplate;
//    private RedisTemplate redisTemplate;

    //@Autowired
    //private JedisCluster cluster;

    @Autowired
    private Redisson redisson;

    /**
     * 设置一个缓存
     * @author yangfan
     * @createTime 2019-11-24 02:48:29
     * @param k 健
     * @param v 值
     */
    @RequestMapping("set")
    public String setVal(String k, String v) {
//        return jedisTemplate.excuteCluster(jedis -> jedis.set(k, v)).toString();
        return jedisTemplate.excuteCommon(jedis -> jedis.set(k, v)).toString();
        /*redisTemplate.opsForValue().set(k, v);
        return "OK";*/
    }

    /**
     * 获取一个缓存
     * @author yangfan
     * @createTime 2019-11-24 02:48:59
     * @param k 健
     * @return java.lang.String
     */
    @RequestMapping("get")
    public String getVal(String k) {
//        return jedisTemplate.excuteCluster(jedis -> jedis.get(k)).toString();
        return jedisTemplate.excuteCommon(jedis -> jedis.get(k)).toString();
//        return String.valueOf(redisTemplate.opsForValue().get(k));
    }

    /**
     * 删除一个缓存
     * @author yangfan
     * @createTime 2019-11-24 02:48:59
     * @param k 健
     * @return java.lang.String
     */
    @RequestMapping("del")
    public String delVal(String k) {
        return jedisTemplate.excuteCluster(jedis -> jedis.del(k)).toString();
//        return jedisTemplate.excuteCommon(jedis -> jedis.del(k)).toString();
        /*redisTemplate.delete(k);
        return "OK";*/
    }

    /**
     * 管道执行命令，管道执行并不是原子性的，只是放在一起与redis服务器进行一次交互减少网络开销，对执行结果是否原子性无关
     * @author yangfan
     * @createTime 2019-11-24 02:51:16
     * @param k 健
     * @param v 值
     * @return java.lang.String
     */
    @RequestMapping("pipeLine")
    public String pipeSet(String k, String v) {
        // 注意：集群模式不支持管道
        return jedisTemplate.excuteCommon(jedis -> {
            Pipeline pipeline = jedis.pipelined();
            pipeline.del("monkey");
            pipeline.set(k + "001", v);
            pipeline.set(k + "002", v);
            pipeline.set(k + "003", v);
            pipeline.set(k + "004", v);
            return pipeline.syncAndReturnAll();
        }).toString();
        /*return redisTemplate.executePipelined((RedisCallback<String>) conn -> {
            conn.del("monkey".getBytes());
            conn.set((k + "001").getBytes(), v.getBytes());
            conn.set((k + "002").getBytes(), v.getBytes());
            conn.set((k + "003").getBytes(), v.getBytes());
            conn.set((k + "004").getBytes(), v.getBytes());
            // 只能返回NULL，返回其他redis上能执行成功，但程序报错
            return null;
        }).toString();*/
    }

    /**
     * 测试lua脚本调用redis
     * @author yangfan
     * @createTime 2019-11-24 02:59:26
     * @param k 健
     * @return java.lang.String
     */
    @RequestMapping("lua")
    public String lua(String k, String v) {
        String script = " local kCount = redis.call('get',KEYS[1]) " +
                " if kCount == 'monkey' then " +
                " redis.call('set',KEYS[1], ARGV[1])" +
                " return 'good' " +
                " end " +
                " return 'sorry, old val is not monkey' ";
        return jedisTemplate.excuteCluster(jedis -> jedis.eval(script, Arrays.asList(k), Arrays.asList(v))).toString();
        /*return jedisTemplate.excuteCommon(jedis -> {
            return jedis.eval(script, Arrays.asList(k), Arrays.asList(v));
        }).toString();*/
        /*RedisScript redisScript = new DefaultRedisScript(script, String.class);//RedisScript.of(script, String.class);
        return String.valueOf(redisTemplate.execute(redisScript, new StringRedisSerializer(),
                new StringRedisSerializer(), Arrays.asList(k), v));*/
    }

    @RequestMapping("buy")
    public String buy() {
        System.out.println(--CountHolder.count);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (CountHolder.count <= 0) {
            CountHolder.count = 50;
        }
        String success = "no ticket", ticketLock = "ticketLock";
        Jedis jedis = null;
        try {
            // 加分布式锁
            jedis = jedisTemplate.getJedisInstence();
            Long lock = jedis.setnx(ticketLock, "1");
            if (lock == 1) {
                int ticket = Integer.parseInt(jedis.get("ticket"));
                if (ticket > 0) {
                    jedis.decr("ticket");
                    success = "success";
                    System.out.println("卖掉一张票，剩余库存：" + --ticket);
                } else {
                    System.out.println("剩余库存不足！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.del(ticketLock);
                jedis.close();
            }
        }

        return success;
    }

    @RequestMapping("buy2")
    public void buy2() {
        //RLock lock = redisson.getLock("redissonLock");

        try (Jedis jedis = jedisTemplate.getJedisInstence()) {
            //lock.lock();
            int ticket = Integer.parseInt(jedis.get("ticket"));
            if (ticket > 0) {
                ticket = ticket - 1;
                Thread.sleep(1000);
                jedis.set("ticket", String.valueOf(ticket));
                System.out.println("卖掉一张票，剩余库存：" + jedis.get("ticket"));
            } else {
                System.out.println("剩余库存不足！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }/* finally {
            lock.unlock();
        }*/
    }
}

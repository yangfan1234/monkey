package com.monkey.home.support;

import com.monkey.home.config.RedisParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * redis连接池初始化条件，根据不同的部署模式来初始化不同的bean， 若为single则初始化单节点连接池
 * @author yangfan
 * @createTime 2019-11-24 9:45
 */
public class RedisModeSigleCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String redisMode = environment.getProperty("monkey.home.redis.mode");
        return StringUtils.isNotBlank(redisMode) && RedisParams.REDIS_MODE_SINGLE.equals(redisMode);
    }
}

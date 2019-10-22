package com.monkey.home.zk.support;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * zk注册条件类
 * @author yangfan
 * @createTime 2019-10-19 20:04
 */
public class ZkAutoRegisterCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String autoRegistry = environment.getProperty("monkey.home.zk.autoRegistry");
        return StringUtils.isBlank(autoRegistry) || "true".equals(autoRegistry);
    }
}

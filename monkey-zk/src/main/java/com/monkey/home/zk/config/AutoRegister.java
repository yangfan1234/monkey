package com.monkey.home.zk.config;

import com.monkey.home.zk.support.MonkeyClient;
import com.monkey.home.zk.support.ZkAutoRegisterCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 服务启动后注册到zk，可配置是否自动注册
 * @author yangfan
 * @createTime 2019-10-19 19:51
 */
@Component
@Conditional(ZkAutoRegisterCondition.class)
public class AutoRegister {

    @Autowired
    private MonkeyClient monkeyClient;

    @Autowired
    private ZkConfigParam config;

    /** 服务注册根节点 */
    private static final String ROOT_PATH_NAME = "/monkey";

    /** 如果没有配置项目注册在zk的节点名称，则使用为默认名称 */
    private static final String REGISTRY_PATH_NAME_DEFAULT = "/monkey/defaultMonkeyProject";

    @PostConstruct
    public void init() {
        // 如果不存在monkey根节点则创建
        if (!monkeyClient.isPathExists(ROOT_PATH_NAME)) {
            monkeyClient.createPersistentNode(ROOT_PATH_NAME, "Root Path of Project Monkey");
        }
        // 如果设置了注册节点名称则使用该名称注册一个临时序号节点，否则使用默认名称创建临时序号节点
        String data = StringUtils.isNotBlank(config.getRegistryData()) ? config.getRegistryData() : "",
        pathName = StringUtils.isNotBlank(config.getRegistryName()) ? config.getRegistryName() : REGISTRY_PATH_NAME_DEFAULT;
        monkeyClient.createEphemeralSequentialNode(pathName, data);
    }
}

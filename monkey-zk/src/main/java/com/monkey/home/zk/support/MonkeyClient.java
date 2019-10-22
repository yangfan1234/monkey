package com.monkey.home.zk.support;

import com.monkey.home.zk.exception.MonkeyZkException;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * zk自定义客户端
 * @author yangfan
 * @createTime 2019-10-18 0:12
 */
@Component
public class MonkeyClient {

    @Autowired
    private ZooKeeper zooKeeper;

    /**
     * 创建持久节点
     * @author yangfan
     * @createTime 2019-10-19 08:10:59
     * @param path 节点名称
     * @param data 节点数据
     * @return void
     */
    public void createPersistentNode(String path, String data) {
        createNode(path, data, CreateMode.PERSISTENT);
    }

    /**
     * 创建临时节点
     * @author yangfan
     * @createTime 2019-10-19 08:10:59
     * @param path 节点名称
     * @param data 节点数据
     * @return void
     */
    public void createEphemeralNode(String path, String data) {
        createNode(path, data, CreateMode.EPHEMERAL);
    }

    /**
     * 创建临时序号节点
     * @author yangfan
     * @createTime 2019-10-19 08:10:59
     * @param path 节点名称
     * @param data 节点数据
     * @return void
     */
    public void createEphemeralSequentialNode(String path, String data) {
        createNode(path, data, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    /**
     * 创建节点
     * @author yangfan
     * @createTime 2019-10-18 11:00:13
     * @param path 节点名称
     * @param data 节点数据
     * @param createMode 创建节点类型
     * @return void
     */
    public void createNode(String path, String data, CreateMode createMode) {
        try {
            zooKeeper.create(path, data.getBytes(), getFullAcls(), createMode);
        } catch (Exception e) {
            throw new MonkeyZkException("创建节点异常", e);
        }
    }

    /**
     * 判断节点名称是否存在
     * @author yangfan
     * @createTime 2019-10-19 08:11:54
     * @param path 节点名称
     * @return boolean
     */
    public boolean isPathExists(String path) {
        Stat stat = null;
        try {
            stat = zooKeeper.exists(path, false);
        } catch (Exception e) {
            throw new MonkeyZkException("判断节点是否存在异常", e);
        }
        return stat != null;
    }

    /**
     * 获取节点下的所有子节点
     * @author yangfan
     * @createTime 2019-10-18 11:05:50
     * @param father 父节点
     * @return java.util.List<java.lang.String>
     */
    public List<String> listChildren(String father) {
        try {
            return zooKeeper.getChildren(father, false);
        } catch (Exception e) {
            throw new MonkeyZkException("获取子节点异常", e);
        }
    }

    /**
     * 设置数据和子节点监听
     * @author yangfan
     * @createTime 2019-10-19 08:36:57
     * @param path 节点
     * @param watcher 监听
     * @return java.util.List<java.lang.String>
     * @throws MonkeyZkException
     */
    public List<String> settingDataAndChildrenWatcher(String path, Watcher watcher) {
        try {
            return zooKeeper.getChildren(path, watcher);
        } catch (Exception e) {
            throw new MonkeyZkException("设置数据和子节点监听异常", e);
        }
    }

    private List<ACL> getFullAcls() {
        List<ACL> acls = new ArrayList<>();
        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("world", "anyone"));
        acls.add(acl);
        return acls;
    }

}

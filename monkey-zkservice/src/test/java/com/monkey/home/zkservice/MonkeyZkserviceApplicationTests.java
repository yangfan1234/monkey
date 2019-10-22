package com.monkey.home.zkservice;

import com.monkey.home.zk.support.MonkeyClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonkeyZkserviceApplicationTests {

    @Autowired
    MonkeyClient monkeyClient;

    @Autowired
    ZkClient zkClient;

    @Test
    public void TestClient() throws IOException {

        // 创建节点
        /*if (!monkeyClient.isPathExists("/monkey")) {
            monkeyClient.createNode("/monkey", "portal of monkey", CreateMode.PERSISTENT);
        }*/

        // 获取节点下的所有子节点
        // List<String> children = monkeyClient.listChildren("/boat");
        /*if (children != null) {
            children.forEach(s -> out.println(s));
        }*/

        // 阻塞线程，模拟项目开启状态
        //int i = System.in.read();

        // 现成的工具用的就是爽，比MonkeyClient好多了
        //zkClient.createPersistent("/zkClient", "zkClient");
        //zkClient.createPersistent("/ccc", new Object());  对象必须实现序列化接口
        Object o = zkClient.readData("/zkClient");
        System.out.println(o);
        System.out.println(zkClient.countChildren("/boat"));
        zkClient.setZkSerializer(new ZkSerializer() {
            @Override
            public byte[] serialize(Object o) throws ZkMarshallingError {
                return String.valueOf(o).getBytes();
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return new String(bytes);
            }
        });
        String o1 = zkClient.readData("/boat");
        System.out.println(o1);
        zkClient.getChildren("/boat").forEach(s -> System.out.println(s));

        // 这种监听不需要重复添加，持久有效
        zkClient.subscribeChildChanges("/boat", (s, list) -> {
            System.out.println(s);
            list.forEach(s1 -> System.out.println(s1));
        });

        System.in.read();

    }

}

package com.monkey.home.zkservice;

import com.alibaba.dubbo.config.annotation.Service;
import com.monkey.home.zk.entity.ZooNodeDto;
import com.monkey.home.zk.support.MonkeyClient;
import com.monkey.home.zk.api.INodesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * zk节点信息服务接口服务
 * @author yangfan
 * @createTime 2019-10-18 0:46
 */
@Component
@Service(version = "1.0.0", timeout = 10000, interfaceClass = INodesDataService.class)
public class NodesDataServiceImpl implements INodesDataService {

    @Autowired
    private MonkeyClient monkeyClient;

    @Override
    public List<ZooNodeDto> childrenList(String parentPath) {
        List<String> children = monkeyClient.listChildren(parentPath);
        List<ZooNodeDto> nodeDtos = new ArrayList<>(128);
        if (children != null && children.size() > 0) {
            children.forEach(child -> {
                ZooNodeDto nodeDto = new ZooNodeDto();
                nodeDto.setPath(child);
                nodeDtos.add(nodeDto);
            });
        }
        return nodeDtos;
    }
}

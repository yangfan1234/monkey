package com.monkey.home.portal.datacontroller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.monkey.home.zk.api.INodesDataService;
import com.monkey.home.zk.entity.ZooNodeDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务信息Controller
 *
 * @author yangfan
 * @createTime 2019-10-19 23:35
 */
@RestController
@RequestMapping("serviceMessage")
public class ServiceMessageController {

    @Reference(version = "1.0.0")
    private INodesDataService nodesDataService;

    @RequestMapping("serviceList")
    public List<ZooNodeDto> serviceList() {
        return nodesDataService.childrenList("/monkey");
    }
}

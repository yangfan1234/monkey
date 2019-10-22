package com.monkey.home.zk.api;


import com.monkey.home.zk.entity.ZooNodeDto;

import java.util.List;

/**
 * zk节点信息服务接口
 *
 * @author yangfan
 * @createTime 2019-10-18 12:38:24
 */
public interface INodesDataService {

    /**
     * 通过父节点名称获取所有子节点集合
     *
     * @param parentPath 父节点名称
     * @return java.util.List<com.monkey.home.zk.dto.ZooNodeDto>
     * @author yangfan
     * @createTime 2019-10-18 12:44:26
     */
    List<ZooNodeDto> childrenList(String parentPath);
}

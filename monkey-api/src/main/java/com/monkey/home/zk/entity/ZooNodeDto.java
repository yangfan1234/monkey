package com.monkey.home.zk.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 节点对象
 * @author yangfan
 * @createTime 2019-10-18 0:40
 */
@Data
public class ZooNodeDto implements Serializable {

    /** 节点名称 */
    private String path;

    /** 节点数据 */
    private String data;

    /** 节点类型 */
    private int type;
}

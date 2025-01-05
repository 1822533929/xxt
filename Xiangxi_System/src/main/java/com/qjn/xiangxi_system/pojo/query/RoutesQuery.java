package com.qjn.xiangxi_system.pojo.query;

import lombok.Data;

@Data
public class RoutesQuery extends BaseQuery{
    /**
     * ID
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 小提示
     */
    private String tips;

    /**
     * 路线内容
     */
    private String content;

    /**
     * 图片
     */
    private String img;

    /**
     * 地点
     */
    private String location;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;
}

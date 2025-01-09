package com.qjn.xiangxi_system.pojo.query;

import lombok.Data;

@Data
public class NewsQuery extends BaseQuery{
    /**
     * 资讯唯一标识符
     */
    private Integer id;

    /**
     * 资讯标题
     */
    private String title;

    /**
     * 封面图片URL
     */
    private String cover;

    /**
     * 资讯摘要
     */
    private String summary;

    /**
     * 资讯内容
     */
    private String content;

    /**
     * 发布时间
     */
    private String publishTime;

    /**
     * 浏览量
     */
    private Integer readCount;
}

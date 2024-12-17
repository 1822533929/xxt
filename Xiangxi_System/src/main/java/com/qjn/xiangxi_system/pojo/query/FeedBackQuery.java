package com.qjn.xiangxi_system.pojo.query;

import java.util.Date;

public class FeedBackQuery extends BaseQuery{
    /**
     * 反馈ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 反馈类型：suggestion-功能建议，bug-问题报告，complaint-投诉，other-其他
     */
    private String type;

    /**
     * 反馈标题
     */
    private String title;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 处理状态：pending-待处理，completed-已完成
     */
    private String status;

    /**
     * 回复内容
     */
    private String reply;

    /**
     * 创建时间
     */
    private Date createTime;

}

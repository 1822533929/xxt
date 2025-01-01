package com.qjn.xiangxi_system.pojo.query;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class OrdersQuery extends BaseQuery{
    /**
     * 订单ID，主键
     */
    private Integer id;

    /**
     * 用户ID，外键关联User表
     */
    private Integer userid;

    /**
     * 旅行ID，外键关联Travels表
     */
    private Integer travelid;

    /**
     * 订单日期
     */
    private Date orderdate;

    /**
     * 订单状态（如：待支付、已支付、已取消等）
     */
    private String status;

    /**
     * 订单总价
     */
    private BigDecimal totalprice;

    /**
     * 购买的旅行套餐数量
     */
    private Integer quantity;
    /**
     * 用户名
     */
    private String username;
    /**
     * 旅行标题
     */
    private String title;
    /**
     * 封面图片
     */
    private String cover;
}

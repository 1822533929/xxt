package com.qjn.xiangxi_system.pojo.query;

import lombok.Data;

@Data
public class OrdersQuery extends BaseQuery {
    private String title;  // 商品名称
    private String orderId;  // 订单编号
}

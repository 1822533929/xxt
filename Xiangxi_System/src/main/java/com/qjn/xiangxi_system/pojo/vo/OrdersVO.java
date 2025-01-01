package com.qjn.xiangxi_system.pojo.vo;

import com.qjn.xiangxi_system.pojo.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrdersVO extends Orders {
    private String username;
    private String title;
    private String cover;
}

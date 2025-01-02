package com.qjn.xiangxi_system.pojo.vo;

import com.qjn.xiangxi_system.pojo.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrdersVO extends Orders {
    private String title;
    private String cover;
    private String username;
    private Integer travelid;
    private BigDecimal money;
}

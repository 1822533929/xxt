package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Orders;
import com.qjn.xiangxi_system.service.OrdersService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qjn.xiangxi_system.utils.DateTimeUtil;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Resource
    OrdersService orderService;
    /**
     * 订单创建
     */
    @RequestMapping("/createOrder")
    public Result createOrder(Integer travelId,Integer userId,Integer quantity,String title,Integer price,String username){
        Orders order = new Orders();
        order.setTravelid(travelId);
        order.setUserid(userId);
        order.setQuantity(quantity);
        order.setStatus("待支付");
        order.setTitle(title);
        order.setUsername(username);
        order.setTotalprice(new BigDecimal(price*quantity));
        order.setOrderdate(DateTimeUtil.getDateTime());
        orderService.save(order);
        return Result.success();
    }
    /**
     * 订单状态修改->已支付
     */
    @RequestMapping("/updateOrder")
    public Result updateOrder(Integer id){
        Orders order = new Orders();
        order.setId(id);
        order.setStatus("已支付");
        orderService.updateById(order);
        return Result.success();
    }
    /**
     * 订单状态修改->已取消
     */
    @RequestMapping("/cancelOrder")
    public Result cancelOrder(Integer id){
        Orders order = new Orders();
        order.setId(id);
        order.setStatus("已取消");
        orderService.updateById(order);
        return Result.success();
    }
    /**
     * 分页查询所有订单
     */

}

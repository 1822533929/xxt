package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qjn.xiangxi_system.pojo.vo.OrdersVO;
import com.qjn.xiangxi_system.pojo.query.OrdersQuery;

import java.util.List;

/**
* @author Administrator
* @description 针对表【order(订单表)】的数据库操作Service
* @createDate 2024-12-30 21:15:43
*/
public interface OrdersService extends IService<Orders> {

    List<OrdersVO> getAllOrders(OrdersQuery query);

    List<OrdersVO> getUserOrders(Integer userId, String title, String orderId);

    void deleteBatch(List<Integer> ids);

    boolean cancelOrder(Integer id);
}

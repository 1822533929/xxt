package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjn.xiangxi_system.pojo.vo.OrdersVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2024-12-30 21:15:43
* @Entity com.qjn.xiangxi_system.pojo.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {

    List<OrdersVO> getAllOrders();

    List<OrdersVO> getUserOrders(Integer userid);
}





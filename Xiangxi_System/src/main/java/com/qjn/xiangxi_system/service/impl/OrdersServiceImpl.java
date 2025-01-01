package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Orders;
import com.qjn.xiangxi_system.pojo.vo.OrdersVO;
import com.qjn.xiangxi_system.service.OrdersService;
import com.qjn.xiangxi_system.mapper.OrdersMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2024-12-30 21:15:43
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService {
    @Resource
    OrdersMapper ordersMapper;

    @Override
    public List<OrdersVO> getAllOrders() {
        return ordersMapper.getAllOrders();
    }

    @Override
    public List<OrdersVO> getUserOrders(Integer userid) {
        return ordersMapper.getUserOrders(userid);
    }
}





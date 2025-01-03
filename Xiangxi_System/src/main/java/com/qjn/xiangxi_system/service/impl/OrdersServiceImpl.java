package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Orders;
import com.qjn.xiangxi_system.pojo.vo.OrdersVO;
import com.qjn.xiangxi_system.service.OrdersService;
import com.qjn.xiangxi_system.mapper.OrdersMapper;
import com.qjn.xiangxi_system.pojo.query.OrdersQuery;
import com.qjn.xiangxi_system.service.TravelsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private OrdersMapper ordersMapper;
    
    @Resource
    private TravelsService travelsService;

    @Override
    public List<OrdersVO> getAllOrders(OrdersQuery query) {
        return ordersMapper.getAllOrders(query);
    }

    @Override
    public List<OrdersVO> getUserOrders(Integer userId, String title, String orderId) {
        return ordersMapper.getUserOrders(userId, title, orderId);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        ordersMapper.deleteBatch(ids);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Integer id) {
        try {
            // 1. 获取订单信息
            Orders order = this.getById(id);
            if (order == null) {
                return false;
            }
            
            // 2. 检查订单状态
            if (!"待支付".equals(order.getStatus())) {
                return false;
            }
            
            // 3. 更新订单状态
            order.setStatus("已取消");
            boolean updated = this.updateById(order);
            
            // 4. 恢复库存
            if (updated) {
                travelsService.releaseInventory(order.getTravelid(), order.getQuantity());
            }
            
            return updated;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("取消订单失败");
        }
    }
}





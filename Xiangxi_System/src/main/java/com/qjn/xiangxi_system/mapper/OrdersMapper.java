package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjn.xiangxi_system.pojo.vo.OrdersVO;
import com.qjn.xiangxi_system.pojo.query.OrdersQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2024-12-30 21:15:43
* @Entity com.qjn.xiangxi_system.pojo.Orders
*/

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    List<OrdersVO> getAllOrders(OrdersQuery query);

    List<OrdersVO> getUserOrders(@Param("userId") Integer userId, 
                                @Param("title") String title, 
                                @Param("orderId") String orderId);

    void deleteBatch(List<Integer> ids);

    @Select("SELECT DATE_FORMAT(orderdate, '%Y-%m-%d') as date, COUNT(*) as count " +
            "FROM orders " +
            "WHERE orderdate >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "GROUP BY date")
    List<Map<String, Object>> getWeeklyOrders();

    @Select("SELECT DATE_FORMAT(orderdate, '%Y-%m') as month, COUNT(*) as count " +
            "FROM orders " +
            "WHERE orderdate >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
            "GROUP BY month")
    List<Map<String, Object>> getMonthlyOrders();
}





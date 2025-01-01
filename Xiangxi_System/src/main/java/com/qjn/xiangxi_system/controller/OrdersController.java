package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Orders;
import com.qjn.xiangxi_system.pojo.Travels;
import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.pojo.query.OrdersQuery;
import com.qjn.xiangxi_system.pojo.vo.OrdersVO;
import com.qjn.xiangxi_system.service.OrdersService;
import com.qjn.xiangxi_system.service.TravelsService;
import com.qjn.xiangxi_system.utils.Result;
import com.qjn.xiangxi_system.utils.UserToken;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qjn.xiangxi_system.utils.DateTimeUtil;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Resource
    OrdersService orderService;
    @Resource
    TravelsService travelsService;
    /**
     * 订单创建
     */
    @RequestMapping("/createOrder")
    public Result createOrder(Integer travelId,
                            @RequestHeader("Authorization") String token, Integer quantity, BigDecimal money) {
        //确认库存充足
        if (!travelsService.checkInventory(travelId, quantity)) {
            return Result.error("库存不足");
        }
        //获取用户信息
        User user = UserToken.parseUserFromToken(token);
        if (user == null || user.getId() == null) {
            return Result.error("无法获取用户信息");
        }
        
        try {
            Orders order = new Orders();
            order.setTravelid(travelId);
            order.setUserid(user.getId());
            order.setQuantity(quantity);
            order.setTotalprice(money.multiply(new BigDecimal(quantity)));
            order.setStatus("待支付");
            order.setOrderdate(DateTimeUtil.getDateTime());
            //库存减少
            travelsService.reduceInventory(travelId, quantity);
            if (orderService.save(order)) {
                return Result.success("订单创建成功");
            } else {
                return Result.error("订单创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("订单创建失败：" + e.getMessage());
        }
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
    @RequestMapping("/getAllOrders")
    public Result<PageInfo<OrdersVO>> getAllOrders(OrdersQuery query){
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<OrdersVO> pageInfo = new PageInfo<>(orderService.getAllOrders());
        return Result.success(pageInfo);
    }
    /**
     *  查询用户订单
     */
    @RequestMapping("/getUserOrders")
    public Result<PageInfo<OrdersVO>> getUserOrders(OrdersQuery query, @RequestHeader("Authorization") String token){
        User user = UserToken.parseUserFromToken(token);
        if (user == null || user.getId() == null) {
            return Result.error("无法获取用户信息");
        }
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<OrdersVO> pageInfo = new PageInfo<>(orderService.getUserOrders(user.getId()));
        return Result.success(pageInfo);
    }
    /**
     * 删除订单
     */
    @RequestMapping("/deleteOrder")
    public Result deleteOrder(Integer id){
        orderService.removeById(id);
        return Result.success();
    }

}

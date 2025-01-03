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
import org.springframework.web.bind.annotation.*;
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
                            @RequestHeader("Authorization") String token, 
                            Integer quantity, 
                            BigDecimal money) {
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
     * 订单状态修改->已完成
     */
    @RequestMapping("/updateOrder")
    public Result updateOrder(Integer id){
        Orders order = new Orders();
        order.setId(id);
        order.setStatus("已完成");
        orderService.updateById(order);
        return Result.success();
    }
    /**
     * 订单状态修改->已取消
     * 取消订单后释放库存
     */
    @RequestMapping("/cancelOrder")
    public Result cancelOrder(@RequestParam Integer id) {
        try {
            if (orderService.cancelOrder(id)) {
                return Result.success("订单取消成功");
            } else {
                return Result.error("订单取消失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("订单取消失败: " + e.getMessage());
        }
    }
    /**
     * 分页查询所有订单
     */
    @RequestMapping("/getAllOrders")
    public Result<PageInfo<OrdersVO>> getAllOrders(OrdersQuery query){
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<OrdersVO> pageInfo = new PageInfo<>(orderService.getAllOrders(query));
        return Result.success(pageInfo);
    }
    /**
     *  查询用户订单
     */
    @RequestMapping("/getUserOrders")
    public Result<PageInfo<OrdersVO>> getUserOrders(OrdersQuery query, 
                                              @RequestHeader("Authorization") String token) {
        User user = UserToken.parseUserFromToken(token);
        if (user == null || user.getId() == null) {
            return Result.error("无法获取用户信息");
        }
        
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<OrdersVO> pageInfo = new PageInfo<>(
            orderService.getUserOrders(user.getId(), query.getTitle(), query.getOrderId())
        );
        return Result.success(pageInfo);
    }
    /**
     * 删除订单
     */
    @RequestMapping("/deleteOrder/{id}")
    public Result deleteOrder(@PathVariable Integer id){
        orderService.removeById(id);
        return Result.success();
    }
    /**
     * 批量删除订单
     */
    @RequestMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("未选择要删除的数据");
        }
        orderService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }

}

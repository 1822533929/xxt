package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.mapper.ArticleMapper;
import com.qjn.xiangxi_system.mapper.OrdersMapper;
import com.qjn.xiangxi_system.mapper.RoutesMapper;
import com.qjn.xiangxi_system.mapper.TravelsMapper;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatisticsController {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TravelsMapper travelsMapper;
    @Resource
    private RoutesMapper routesMapper;
    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 获取总览数据
     */
    @GetMapping("/overview")
    public Result getOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("guideCount", articleMapper.selectCount(null));
        data.put("productCount", travelsMapper.selectCount(null));
        data.put("routeCount", routesMapper.selectCount(null));
        data.put("orderCount", ordersMapper.selectCount(null));
        return Result.success(data);
    }

    /**
     * 获取订单趋势数据
     */
    @GetMapping("/orderTrend")
    public Result getOrderTrend(@RequestParam String type) {
        List<Map<String, Object>> data;
        if ("week".equals(type)) {
            data = ordersMapper.getWeeklyOrders();
        } else {
            data = ordersMapper.getMonthlyOrders();
        }
        return Result.success(data);
    }

    /**
     * 获取商品销量排行
     */
    @GetMapping("/productRank")
    public Result getProductRank() {
        List<Map<String, Object>> data = travelsMapper.getProductRank();
        return Result.success(data);
    }
}

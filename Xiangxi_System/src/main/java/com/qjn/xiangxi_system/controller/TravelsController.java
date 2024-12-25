package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.Travels;
import com.qjn.xiangxi_system.pojo.query.TravelsQuery;
import com.qjn.xiangxi_system.service.TravelsService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/travels")
public class TravelsController {
    @Resource
    private TravelsService travelsService;
    /**
     * 热门景点
     * 根据阅读量从高到低查询旅游信息
     */
    @RequestMapping("/selectAllByRead")
    public Result<PageInfo<Travels>> selectAllByRead(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "3") Integer pageSize
    ) {
        TravelsQuery query = new TravelsQuery();
        query.setCurrentPage(currentPage);
        query.setPageSize(pageSize);
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<Travels> pageInfo = new PageInfo<>(travelsService.selectAllByRead());
        return Result.success(pageInfo);
    }
    /**
     * 全部景点
     * 分页查询所有旅游商品
     */
    @RequestMapping("/selectAll")
    public Result<PageInfo<Travels>> selectAll(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "6") Integer pageSize
    ) {
        TravelsQuery query = new TravelsQuery();
        query.setCurrentPage(currentPage);
        query.setPageSize(pageSize);
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<Travels> pageInfo = new PageInfo<>(travelsService.list());
        return Result.success(pageInfo);
    }

}

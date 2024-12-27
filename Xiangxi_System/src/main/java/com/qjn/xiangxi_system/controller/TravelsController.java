package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Travels;
import com.qjn.xiangxi_system.pojo.query.BaseQuery;
import com.qjn.xiangxi_system.pojo.query.TravelsQuery;
import com.qjn.xiangxi_system.pojo.vo.TravelsVO;
import com.qjn.xiangxi_system.service.TravelsService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 修改旅游商品
     */
    @RequestMapping("/update")
    public Result update(Travels travels) {
        if (travelsService.updateById(travels)) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    /**
     * 删除旅游商品
     */
    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        if (travelsService.removeById(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    /**
     * 批量删除旅游商品
     */
    @RequestMapping ("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids)
    {
        if (ids == null || ids.isEmpty()) {
            return Result.error("未选择要删除的数据");
        }
        travelsService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 根据id查询旅游商品
     */
    @RequestMapping("/selectById/{id}")
    public Result selectById(@PathVariable("id") Integer id)
    {
        Travels travels = travelsService.getById(id);
        return Result.success(travels);
    }
    /**
     * 浏览量增加
     * ps:进入到具体商品页面后增加浏览量
     */
    @RequestMapping("/addRead/{id}")
    public Result addRead(@PathVariable Integer id) {
        Travels travels = travelsService.getById(id);
        travels.setReadCount(travels.getReadCount() + 1);
        travelsService.updateById(travels);
        return Result.success();
    }
    /**
     * 查询某个商品的所有标签
     */
    @RequestMapping("/selectTags/{id}")
    public Result selectTags(@PathVariable Integer id) {
        List<String> tags = travelsService.selectTags(id);
        return Result.success(tags);
    }
    /**
     * 后台查询所有旅游商品及附带标签
     */
    @RequestMapping("/admin/selectAll")
    public Result<PageInfo<TravelsVO>> adminSelectAll(BaseQuery query) {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<TravelsVO> pageInfo = new PageInfo<>(travelsService.SelectAll());
        return Result.success(pageInfo);
    }
    /**
     * 后台添加商品
     */
    @RequestMapping("/admin/add")
    public Result add(@RequestBody TravelsVO travelsVO) {
        if (travelsService.saveWithTags(travelsVO)) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }
    /**
     * 后台修改商品信息
     */





}

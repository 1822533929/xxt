package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.Travels;
import com.qjn.xiangxi_system.pojo.query.BaseQuery;
import com.qjn.xiangxi_system.pojo.query.TravelsQuery;
import com.qjn.xiangxi_system.pojo.vo.TravelsVO;
import com.qjn.xiangxi_system.service.TagsService;
import com.qjn.xiangxi_system.service.TravelsService;
import com.qjn.xiangxi_system.utils.DateTimeUtil;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/travels")
public class TravelsController {
    @Resource
    private TravelsService travelsService;
    @Resource
    private TagsService tagsService;
    @Resource
    private FileUploadUtil fileUploadUtil;
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
    public Result<PageInfo<TravelsVO>> selectAll(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "6") Integer pageSize,
        @RequestParam(required = false) String keyword
    ) {
        PageHelper.startPage(currentPage, pageSize);
        PageInfo<TravelsVO> pageInfo = new PageInfo<>(travelsService.selectAllWithTags(keyword));
        return Result.success(pageInfo);
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
    public Result<PageInfo<TravelsVO>> adminSelectAll(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) String keyword
    ) {
        PageHelper.startPage(currentPage, pageSize);
        PageInfo<TravelsVO> pageInfo = new PageInfo<>(travelsService.selectAllWithTags(keyword));
        return Result.success(pageInfo);
    }
    /**
     * 后台添加商品
     * bug修复：如果请求中包含文件上传（如 MultipartFile），则不能使用 @RequestBody 注解，因为 @RequestBody 期望的是 JSON 格式的数据。
     * 可以使用 @ModelAttribute 来绑定表单数据。
     */
    @RequestMapping("/admin/add")
    public Result add(@ModelAttribute TravelsVO travelsVO,
                      @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try{
            String cover = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                cover = fileUploadUtil.uploadImage(image, "travelCover");
            }
            travelsVO.setTime(DateTimeUtil.getDateTime());
            travelsVO.setCover(cover);
            if (travelsService.saveWithTags(travelsVO)) {
                return Result.success("添加成功");
            }
            return Result.error("添加失败");
        } catch (IOException e) {
            return Result.error("图片上传失败");
        }
    }
    /**
     * 后台修改商品
     */
    @RequestMapping("/admin/update")
    public Result update(@RequestBody TravelsVO travelsVO) {
        if (travelsService.updateWithTags(travelsVO)) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    /**
     * 后台删除商品
     * 删除对应订单（待更改）
     */
    @RequestMapping("/admin/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        if (travelsService.removeWithTags(id)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    /**
     * 后台批量删除商品
     * 删除对应订单（待更改）
     */
    @RequestMapping("/admin/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("未选择要删除的数据");
        }
        travelsService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }
    /**
     * 获取所有标签信息
     */
    @RequestMapping("/getAllTags")
    public Result getTags() {
        return Result.success(tagsService.getAllTags());
    }


   
}

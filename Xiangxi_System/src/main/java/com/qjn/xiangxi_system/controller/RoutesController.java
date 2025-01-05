package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Routes;
import com.qjn.xiangxi_system.pojo.query.RoutesQuery;
import com.qjn.xiangxi_system.service.RoutesService;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/routes")
public class RoutesController {
    @Resource
    private RoutesService routesService;
    @Resource
    private FileUploadUtil fileUploadUtil;

    /**
     * 分页查询所有路线
     */
    @RequestMapping("/selectAll")
    public Result selectAll(RoutesQuery query) {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<Routes> pageInfo = new PageInfo<>(routesService.list());
        return Result.success(pageInfo);
    }
    /**
     * 单个删除
     */
    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        routesService.removeById(id);
        return Result.success();
    }
    /**
     * 批量删除
     */
    @RequestMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        routesService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 添加路线
     */
    @RequestMapping("/add")
    public Result add(Routes routes,
                      @RequestParam(value = "image", required = false) MultipartFile image) {
        try{
            String cover = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                cover = fileUploadUtil.uploadImage(image);
            }
            routes.setImg(cover);
            if (routesService.save(routes)) {
                return Result.success("添加成功");
            }
            return Result.error("添加失败");
        } catch (IOException e) {
            return Result.error("图片上传失败");
        }
    }
    /**
     * 修改路线
     */
    @RequestMapping("/update")
    public Result update(Routes routes) {
        routesService.updateById(routes);
        return Result.success();
    }

}

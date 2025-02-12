package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.News;
import com.qjn.xiangxi_system.pojo.query.NewsQuery;
import com.qjn.xiangxi_system.service.NewsService;
import com.qjn.xiangxi_system.utils.DateTimeUtil;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private NewsService newsService;
    @Resource
    private FileUploadUtil fileUploadUtil;

    /**
     * 获取所有资讯数据
     */
    @RequestMapping("/getNewsList")
    public Result getNewsList(NewsQuery query) {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<News> pageInfo = new PageInfo<>(newsService.list());
        return Result.success(pageInfo);
    }
    /**
     * 获取单个资讯数据
     */
    @RequestMapping("/getNews")
    public Result getNews(Integer id) {
        News news = newsService.getById(id);
        return Result.success(news);
    }
    /**
     * 添加资讯数据
     */
    @RequestMapping("/addNews")
    public Result addNews(News news,
                          @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String cover = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                cover = fileUploadUtil.uploadImage(image,"newsCover");
            }
            news.setCover(cover);
        } catch (IOException e) {
            return Result.error("图片上传失败");
        }
        newsService.save(news);
        return Result.success();
    }
    /**
     * 修改资讯数据
     */
    @RequestMapping("/updateNews")
    public Result updateNews(@ModelAttribute News news,@RequestParam(value = "image", required = false) MultipartFile image) {
        try{
            String cover = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                cover = fileUploadUtil.uploadImage(image, "newsCover");
            }
            news.setCover(cover);
        }catch (IOException e){
            return Result.error("图片上传失败");
        }
        newsService.updateById(news);
        return Result.success();
    }
    /**
     * 删除资讯数据
     */
    @RequestMapping("/deleteNews")
    public Result deleteNews(Integer id) {
        newsService.removeById(id);
        return Result.success();
    }
    /**
     * 批量删除资讯数据
     */
    @RequestMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        newsService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 浏览量增加
     */
    @RequestMapping("/addRead")
    public Result addRead(Integer id) {
        News news = newsService.getById(id);
        news.setReadCount(news.getReadCount() + 1);
        newsService.updateById(news);
        return Result.success();
    }
    /**
     * 获取最新资讯数据
     */
    @RequestMapping("/getNewsByTime")
    public Result getNewsListByTime() {
        PageInfo<News> pageInfo = new PageInfo<>(newsService.getNewsByTime());
        return Result.success(pageInfo);
    }

}

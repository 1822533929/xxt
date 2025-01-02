package com.qjn.xiangxi_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.query.ArticleQuery;
import com.qjn.xiangxi_system.service.ArticleService;
import com.qjn.xiangxi_system.utils.DateTimeUtil;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/add")
    public Result add( @RequestParam(value = "image", required = false) MultipartFile image,
                       @RequestParam("descr") String descr,
                       @RequestParam("title") String title,
                       @RequestParam("content") String content){
       try{
        String cover = null;
        // 只在有图片且不是空文件时处理图片上传
        if (image != null && !image.isEmpty() && image.getSize() > 0) {
            cover = fileUploadUtil.uploadImage(image);
        }
        Article article=new Article();
        article.setTitle(title);
        article.setDescr(descr);
        article.setCover(cover);
        article.setContent(content);
        article.setLikes(0);
        article.setDate(DateTimeUtil.getDateTime());
        articleService.save(article);
        return Result.success();
    } catch (IOException e) {
        return Result.error("图片上传失败");
        }
    }
    @RequestMapping("/update")
    public Result update(Article article)
    {
        articleService.updateById(article);
        return Result.success();
    }
    /**
    单个删除
     */
    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id)
    {
        articleService.removeById(id);
        return Result.success();
    }
    /**
    批量删除
     */
    @RequestMapping ("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids)
    {
        if (ids == null || ids.isEmpty()) {
            return Result.error("未选择要删除的数据");
        }
        articleService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 单个查询
     */
    @RequestMapping("/selectById/{id}")
    public Result selectById(@PathVariable("id") Integer id)
    {
        Article article = articleService.getById(id);
        return Result.success(article);
    }
    /**
     * 全部查询
     */
    @RequestMapping("/selectAll")
    public Result selectAll()
    {
        List<Article> list = articleService.list();
        return Result.success(list);
    }
    /**
     * 分页查询
     */
    @RequestMapping("/selectPage")
    public Result<PageInfo<Article>> selectPage(ArticleQuery query)
    {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<Article> pageInfo = new PageInfo<>(articleService.list());
        return Result.success(pageInfo);
    }
    /**
     * 点赞文章
     */
    @PostMapping("/like/{id}")
    public Result likeArticle(@PathVariable Integer id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        article.setLikes(article.getLikes() + 1);
        articleService.updateById(article);
        return Result.success();
    }
    /**
     * 模糊查询
     * 好像得分页
     */
    @RequestMapping("/search")
    public Result search(@RequestParam("keyword") String keyword) {
        List<Article> list = articleService.list(new QueryWrapper<Article>().like("title", keyword));
        return Result.success(list);
    }
}

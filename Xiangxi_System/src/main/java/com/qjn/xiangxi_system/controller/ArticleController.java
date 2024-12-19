package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.query.ArticleQuery;
import com.qjn.xiangxi_system.service.ArticleService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @RequestMapping("/add")
    public Result add(Article article)
    {
        articleService.save(article);
        return Result.success();
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
    public Result delete(Integer id)
    {
        articleService.removeById(id);
        return Result.success();
    }
    /**
    批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(List<Integer> ids)
    {
        articleService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 单个查询
     */
    @RequestMapping("/selectById/{id}")
    public Result selectById(@PathVariable("id") Integer id)
    {
        System.out.println("接收到查询请求，id: " + id);
        Article article = articleService.getById(id);
        System.out.println("查询结果: " + article);
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
}

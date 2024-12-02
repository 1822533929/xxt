package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.service.ArticleService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result selectById(Integer id)
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
//    @RequestMapping("/selectPage")
//    public Result selectPage(Integer pageNum, Integer pageSize)
//    {
//        return Result.suc(articleService.selectPage(pageNum, pageSize));
//    }
}

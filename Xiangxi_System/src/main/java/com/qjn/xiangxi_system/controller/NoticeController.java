package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.Notice;
import com.qjn.xiangxi_system.service.NoticeService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;
    @RequestMapping("/add")
    public Result add(Notice notice){
        noticeService.save(notice);
        return Result.success();
    }

    @RequestMapping("/update")
    public Result update(Notice notice)
    {
        noticeService.updateById(notice);
        return Result.success();
    }
    /**
     单个删除
     */
    @RequestMapping("/delete/{id}")
    public Result delete(Integer id)
    {
        noticeService.removeById(id);
        return Result.success();
    }
    /**
     批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(List<Integer> ids)
    {
        noticeService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 单个查询
     */
    @RequestMapping("/selectById/{id}")
    public Result selectById(Integer id)
    {
        Notice notice = noticeService.getById(id);
        return Result.success(notice);
    }
    /**
     * 全部查询
     */
    @GetMapping("/selectAll")
    public Result selectAll()
    {
        List<Notice> noticeList = noticeService.list();
        return Result.success(noticeList);
    }
}

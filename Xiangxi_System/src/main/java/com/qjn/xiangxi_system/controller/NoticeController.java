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
        return Result.suc();
    }

    @RequestMapping("/update")
    public Result update(Notice notice)
    {
        noticeService.updateById(notice);
        return Result.suc();
    }
    /**
     单个删除
     */
    @RequestMapping("/delete/{id}")
    public Result delete(Integer id)
    {
        noticeService.removeById(id);
        return Result.suc();
    }
    /**
     批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(List<Integer> ids)
    {
        noticeService.deleteBatch(ids);
        return Result.suc();
    }
    /**
     * 单个查询
     */
    @RequestMapping("/selectById/{id}")
    public Result selectById(Integer id)
    {
        Notice notice = noticeService.getById(id);
        return Result.suc(notice);
    }
    /**
     * 全部查询
     */
    @GetMapping("/selectAll")
    public Result selectAll()
    {
        List<Notice> noticeList = noticeService.list();
        return Result.suc(noticeList);
    }
}

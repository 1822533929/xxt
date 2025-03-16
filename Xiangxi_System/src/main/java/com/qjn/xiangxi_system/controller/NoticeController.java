package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.Notice;
import com.qjn.xiangxi_system.service.NoticeService;
import com.qjn.xiangxi_system.utils.DateTimeUtil;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    @RequestMapping("/add")
    public Result add(@RequestBody Notice notice) {
        notice.setTime(DateTimeUtil.getDateTime());
        noticeService.save(notice);
        return Result.success();
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    @RequestMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        noticeService.deleteBatch(ids);
        return Result.success();
    }

    @RequestMapping("/selectById")
    public Result selectById(@RequestParam Integer id) {
        Notice notice = noticeService.getById(id);
        return Result.success(notice);
    }

    @GetMapping("/selectAll")
    public Result<PageInfo<Notice>> selectAll(
        @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageHelper.startPage(currentPage, pageSize);
        PageInfo<Notice> pageInfo = new PageInfo<>(noticeService.list());
        return Result.success(pageInfo);
    }
    /**
     * 模糊查询
     */
    @RequestMapping("/search")
    public Result search(@RequestParam String keyword) {
        List<Notice> list = noticeService.Search(keyword);
        return Result.success(list);
    }
}

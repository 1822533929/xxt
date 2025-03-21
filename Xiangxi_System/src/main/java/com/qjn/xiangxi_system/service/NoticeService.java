package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【notice】的数据库操作Service
* @createDate 2024-11-24 21:17:13
*/
public interface NoticeService extends IService<Notice> {
    void deleteBatch(List<Integer> ids);

    List<Notice> Search(String keyword);
}

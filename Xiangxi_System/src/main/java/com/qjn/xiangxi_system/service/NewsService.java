package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.News;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【news(资讯数据表)】的数据库操作Service
* @createDate 2025-01-09 21:39:38
*/
public interface NewsService extends IService<News> {

    void deleteBatch(List<Integer> ids);

    List<News> getNewsByTime();
}

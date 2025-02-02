package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【article】的数据库操作Service
* @createDate 2024-11-24 21:16:29
*/
public interface ArticleService extends IService<Article> {
    void deleteBatch(List<Integer> ids);

    List<Article> findHot();
}

package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.News;
import com.qjn.xiangxi_system.service.NewsService;
import com.qjn.xiangxi_system.mapper.NewsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【news(资讯数据表)】的数据库操作Service实现
* @createDate 2025-01-09 21:39:38
*/
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
    implements NewsService{
    @Resource
    private NewsMapper newsMapper;

    @Override
    public void deleteBatch(List<Integer> ids) {
        newsMapper.deleteBatch(ids);
    }
}





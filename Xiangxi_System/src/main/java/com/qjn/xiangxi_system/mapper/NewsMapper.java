package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【news(资讯数据表)】的数据库操作Mapper
* @createDate 2025-01-09 21:39:38
* @Entity com.qjn.xiangxi_system.pojo.News
*/
public interface NewsMapper extends BaseMapper<News> {

    void deleteBatch(List<Integer> ids);
}





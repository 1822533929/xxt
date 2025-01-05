package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Routes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【routes】的数据库操作Mapper
* @createDate 2025-01-05 16:39:05
* @Entity com.qjn.xiangxi_system.pojo.Routes
*/
public interface RoutesMapper extends BaseMapper<Routes> {

    void deleteBatch(List<Integer> ids);
}





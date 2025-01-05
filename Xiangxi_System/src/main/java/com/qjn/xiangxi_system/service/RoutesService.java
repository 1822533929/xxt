package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Routes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【routes】的数据库操作Service
* @createDate 2025-01-05 16:39:05
*/
public interface RoutesService extends IService<Routes> {

    void deleteBatch(List<Integer> ids);
}

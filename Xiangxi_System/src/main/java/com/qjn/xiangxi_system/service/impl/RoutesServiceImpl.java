package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Routes;
import com.qjn.xiangxi_system.service.RoutesService;
import com.qjn.xiangxi_system.mapper.RoutesMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【routes】的数据库操作Service实现
* @createDate 2025-01-05 16:39:05
*/
@Service
public class RoutesServiceImpl extends ServiceImpl<RoutesMapper, Routes>
    implements RoutesService{
    @Resource
    private RoutesMapper routesMapper;

    @Override
    public void deleteBatch(List<Integer> ids) {
        routesMapper.deleteBatch(ids);
    }
}





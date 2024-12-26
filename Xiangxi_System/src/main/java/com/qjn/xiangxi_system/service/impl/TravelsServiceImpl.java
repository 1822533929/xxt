package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.mapper.TravelsMapper;
import com.qjn.xiangxi_system.pojo.Travels;
import com.qjn.xiangxi_system.service.TravelsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author Administrator
* @description 针对表【travels】的数据库操作Service实现
* @createDate 2024-12-25 20:33:50
*/
@Service
public class TravelsServiceImpl extends ServiceImpl<TravelsMapper, Travels>
    implements TravelsService {
    
    @Resource
    private TravelsMapper travelsMapper;

    @Override
    public List<Travels> selectAllByRead() {
        return travelsMapper.selectAllByRead();
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        travelsMapper.deleteBatch(ids);
    }

    @Override
    public List<String> selectTags(Integer id) {
        try {
            List<String> tags = travelsMapper.selectTags(id);
            return tags != null ? tags : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}





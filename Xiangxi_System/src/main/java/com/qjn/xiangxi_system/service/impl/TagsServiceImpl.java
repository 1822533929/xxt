package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Tags;
import com.qjn.xiangxi_system.service.TagsService;
import com.qjn.xiangxi_system.mapper.TagsMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tags】的数据库操作Service实现
* @createDate 2024-12-29 16:43:16
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
    implements TagsService{
    @Resource
    private TagsMapper tagsMapper;

    @Override
    public List<String> getAllTags() {
        return tagsMapper.getAllTags();
    }
}





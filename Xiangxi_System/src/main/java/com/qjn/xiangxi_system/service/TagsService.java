package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Tags;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tags】的数据库操作Service
* @createDate 2024-12-29 16:43:16
*/
public interface TagsService extends IService<Tags> {

    List<String> getAllTags();
}

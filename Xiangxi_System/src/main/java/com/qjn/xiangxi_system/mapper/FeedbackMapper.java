package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【feedback(问题反馈表)】的数据库操作Mapper
* @createDate 2024-12-06 21:53:59
* @Entity com.qjn.xiangxi_system.pojo.Feedback
*/
public interface FeedbackMapper extends BaseMapper<Feedback> {
    List<Feedback> getFeedbacksByUserId(Integer userId);
}





package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【feedback(问题反馈表)】的数据库操作Service
* @createDate 2024-12-06 21:53:59
*/
public interface FeedbackService extends IService<Feedback> {

    boolean addFeedback(Feedback feedback);

    List<Feedback> getUserFeedback(Integer userId);

    List<Feedback> getAllFeedback();
}

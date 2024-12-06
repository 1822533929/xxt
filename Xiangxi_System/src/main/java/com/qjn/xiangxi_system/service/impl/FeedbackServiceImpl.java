package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Feedback;
import com.qjn.xiangxi_system.service.FeedbackService;
import com.qjn.xiangxi_system.mapper.FeedbackMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
* @author Administrator
* @description 针对表【feedback(问题反馈表)】的数据库操作Service实现
* @createDate 2024-12-06 21:53:59
*/
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback>
    implements FeedbackService{

    @Override
    public boolean addFeedback(Feedback feedback) {
        // 设置创建时间
        feedback.setCreateTime(LocalDateTime.now());
        // 设置初始状态为"待处理"
        feedback.setStatus("待处理");
        // 保存反馈信息
        return this.save(feedback);
    }
}





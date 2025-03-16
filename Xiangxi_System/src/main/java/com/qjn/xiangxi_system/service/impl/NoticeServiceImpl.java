package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Notice;
import com.qjn.xiangxi_system.service.NoticeService;
import com.qjn.xiangxi_system.mapper.NoticeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-11-24 21:17:13
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public void deleteBatch(List<Integer> ids) {
        noticeMapper.deleteBatch(ids);
    }

    @Override
    public List<Notice> Search(String keyword) {
        return noticeMapper.Search(keyword);
    }
}





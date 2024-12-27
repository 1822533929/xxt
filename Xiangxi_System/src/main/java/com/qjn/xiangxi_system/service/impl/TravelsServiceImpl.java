package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.mapper.TravelsMapper;
import com.qjn.xiangxi_system.pojo.Travels;
import com.qjn.xiangxi_system.pojo.vo.TravelsVO;
import com.qjn.xiangxi_system.service.TravelsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<TravelsVO> SelectAll() {
        return travelsMapper.SelectAll();
    }

    @Override
    @Transactional
    public boolean saveWithTags(TravelsVO travelsVO) {
        try {
            // 保存旅游商品基本信息
            boolean saved = this.save(travelsVO);
            if (!saved) {
                return false;
            }
            // 处理标签
            if (travelsVO.getTags() != null && !travelsVO.getTags().isEmpty()) {
                String[] tagNames = travelsVO.getTags().split(",");
                List<Integer> tagIds = new ArrayList<>();

                for (String tagName : tagNames) {
                    tagName = tagName.trim();
                    // 查找标签是否存在
                    Integer tagId = travelsMapper.getTagIdByName(tagName);
                    if (tagId == null) {
                        // 如果标签不存在，创建新标签
                        travelsMapper.insertTag(tagName);
                        tagId = travelsMapper.getTagIdByName(tagName);
                    }
                    tagIds.add(tagId);
                }

                // 保存关联关系
                if (!tagIds.isEmpty()) {
                    travelsMapper.insertTravelTags(travelsVO.getId(), tagIds);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存旅游商品失败");
        }
    }
}





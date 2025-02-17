package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Article;
import com.qjn.xiangxi_system.pojo.vo.ArticleVO;
import com.qjn.xiangxi_system.service.ArticleService;
import com.qjn.xiangxi_system.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【article】的数据库操作Service实现
* @createDate 2024-11-24 21:16:29
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void deleteBatch(List<Integer> ids) {
        articleMapper.deleteBatch(ids);
    }

    @Override
    public List<ArticleVO> findHot() {
        return articleMapper.findHot();
    }

    @Override
    public ArticleVO selectByIdWithUser(Integer id) {
        return articleMapper.selectByIdWithUser(id);
    }

    @Override
    public List<ArticleVO> selectAllWithUser() {
        return articleMapper.selectAllWithUser();
    }

    @Override
    public List<Article> findByUserId(Integer userId) {
        return articleMapper.findByUserId(userId);
    }
}





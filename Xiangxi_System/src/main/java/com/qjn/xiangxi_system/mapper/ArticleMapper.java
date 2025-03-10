package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjn.xiangxi_system.pojo.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【article】的数据库操作Mapper
* @createDate 2024-11-24 21:16:29
* @Entity com.qjn.xiangxi_system.pojo.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    void deleteBatch(List<Integer> ids);

    List<ArticleVO> findHot();

    ArticleVO selectByIdWithUser(Integer id);

    List<ArticleVO> selectAllWithUser();

    List<Article> findByUserId(Integer userId);
}





package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Comments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjn.xiangxi_system.pojo.vo.CommentsVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【comments(存储文章的评论信息)】的数据库操作Mapper
* @createDate 2025-02-18 22:25:54
* @Entity com.qjn.xiangxi_system.pojo.Comments
*/
public interface CommentsMapper extends BaseMapper<Comments> {

    List<CommentsVO> getCommentsByArticleId(Integer articleId);

    void likeComment(Integer commentId);

    Boolean deleteComment(Integer commentId);
    void deleteChildComment(Integer commentId);
}





package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Comments;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【comments(存储文章的评论信息)】的数据库操作Service
* @createDate 2025-02-18 22:25:54
*/
public interface CommentsService extends IService<Comments> {

    List<Comments> getCommentsByArticleId(Integer articleId);

    void likeComment(Integer commentId);

    Boolean deleteComment(Integer commentId);

    void addComment(Integer articleId, String content, Integer userId, Integer parentId);
}

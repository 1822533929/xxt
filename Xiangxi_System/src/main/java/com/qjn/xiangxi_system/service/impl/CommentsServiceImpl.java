package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Comments;
import com.qjn.xiangxi_system.service.CommentsService;
import com.qjn.xiangxi_system.mapper.CommentsMapper;
import com.qjn.xiangxi_system.utils.DateTimeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【comments(存储文章的评论信息)】的数据库操作Service实现
* @createDate 2025-02-18 22:25:54
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{
    @Resource
    private CommentsMapper commentsMapper;
    @Override
    public List<Comments> getCommentsByArticleId(Integer articleId) {
        return commentsMapper.getCommentsByArticleId(articleId);
    }

    @Override
    public void likeComment(Integer commentId) {
        commentsMapper.likeComment(commentId);
    }

    @Override
    public Boolean deleteComment(Integer commentId) {
        //删除子评论
        commentsMapper.deleteChildComment(commentId);
        return commentsMapper.deleteComment(commentId);
    }

    @Override
    public void addComment(Integer articleId, String content, Integer userId, Integer parentId) {
        Comments comments = new Comments();
        comments.setArticleId(articleId);
        comments.setContent(content);
        comments.setUserId(userId);
        if(parentId!=null)
        comments.setParentId(parentId);
        comments.setCreatedAt(DateTimeUtil.getDateTime());
        commentsMapper.insert(comments);
    }
}





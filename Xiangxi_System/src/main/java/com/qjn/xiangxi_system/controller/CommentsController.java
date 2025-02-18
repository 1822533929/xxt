package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.service.CommentsService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentsController {
    @Resource
    private CommentsService commentsService;
    /**
     * 获取文章对应的所有评论
     */
    @RequestMapping("/getCommentsByArticleId")
    public Result getCommentsByArticleId(Integer articleId){
        return Result.success(commentsService.getCommentsByArticleId(articleId));
    }
    /**
     * 点赞评论
     */
    @RequestMapping("/like/{commentId}")
    public Result likeComment(@PathVariable Integer commentId){
        commentsService.likeComment(commentId);
        return Result.success();
    }
    /**
     * 删除评论
     * 特殊处理：如果删除的评论是父评论，则删除所有子评论
     */
    @RequestMapping("/delete/{commentId}")
    public Result deleteComment(@PathVariable  Integer commentId){
        return Result.success(commentsService.deleteComment(commentId));
    }
    /**
     * 添加评论
     */
    @RequestMapping("/add")
    public Result addComment(Integer articleId,String content,Integer userId,Integer parentId){
        commentsService.addComment(articleId,content,userId,parentId);
        return Result.success();
    }


}

package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.Feedback;
import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.pojo.query.FeedBackQuery;
import com.qjn.xiangxi_system.pojo.vo.FeedbackVO;
import com.qjn.xiangxi_system.service.FeedbackService;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.JWTUtils;
import com.qjn.xiangxi_system.utils.Result;
import com.qjn.xiangxi_system.utils.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @PostMapping("/add")
    public Result addFeedback(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("type") String type,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "contact", required = false) String contact,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String imageUrl = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                imageUrl = fileUploadUtil.uploadImage(image, "feedback");
            }
            //获取用户数据
            User user = UserToken.parseUserFromToken(token);
            if (user == null || user.getId() == null) {
                return Result.error("无法获取用户信息");
            }
            // 创建反馈对象
            Feedback feedback = new Feedback();
            feedback.setUserId(user.getId());
            feedback.setType(type);
            feedback.setTitle(title);
            feedback.setContent(content);
            feedback.setContact(contact);
            feedback.setImageUrl(imageUrl);

            // 保存反馈
            boolean success = feedbackService.addFeedback(feedback);
            
            if (success) {
                return Result.success("反馈上传成功");
            } else {
                return Result.error("反馈上传失败");
            }
        } catch (IOException e) {
            return Result.error("图片上传失败");
        }
    }
    @GetMapping("/getUserFeedback")
    public Result getUserFeedback(@RequestHeader("Authorization") String token) {
        try {
            User user = UserToken.parseUserFromToken(token);
            if (user == null || user.getId() == null) {
                return Result.error("无法获取用户信息");
               }
            // 查询该用户的反馈
            return Result.success(feedbackService.getUserFeedback(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取反馈失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询所有反馈
     * @param query
     * @return
     */
    @GetMapping("/getFeedbackList")
    public Result<PageInfo<FeedbackVO>> getFeedbackList(FeedBackQuery query) {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<FeedbackVO> pageInfo = new PageInfo<>(feedbackService.getAllFeedback());
        return Result.success(pageInfo);
    }
    /**
     * 回复反馈
     */
    @PostMapping("/reply")
    public Result replyFeedback(@RequestParam("id") Integer id,
                                @RequestParam("reply") String reply) {
        Feedback feedback = feedbackService.getById(id);
        if (feedback == null) {
            return Result.error("反馈不存在");
        }
        feedback.setReply(reply);
        feedback.setStatus("已处理");
        boolean success = feedbackService.updateById(feedback);
        if (success) {
            return Result.success("回复成功");
        } else {
            return Result.error("回复失败");
        }
    }
    /**
     * 删除反馈
     */
    @PostMapping("/deleteFeedback")
    public Result deleteFeedback(@RequestParam("id") Integer id) {
        boolean success = feedbackService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
    /**
     * 根据是否处理筛选反馈
     */
    @GetMapping("/filterFeedback")
    public Result<PageInfo<FeedbackVO>> filterFeedback(@RequestParam("status") String status,FeedBackQuery query) {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<FeedbackVO> pageInfo = new PageInfo<>(feedbackService.getFeebackdByStatus(status));
        return Result.success(pageInfo);
    }
} 
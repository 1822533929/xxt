package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Feedback;
import com.qjn.xiangxi_system.pojo.User;
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
                imageUrl = fileUploadUtil.uploadImage(image);
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
} 
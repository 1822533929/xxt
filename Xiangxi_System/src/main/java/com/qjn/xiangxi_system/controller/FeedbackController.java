package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Feedback;
import com.qjn.xiangxi_system.service.FeedbackService;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            @RequestParam("userId") Integer userId,
            @RequestParam("type") String type,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "contact", required = false) String contact
    ) {
        try {
            String imageUrl = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                imageUrl = fileUploadUtil.uploadImage(image);
            }
            
            // 创建反馈对象
            Feedback feedback = new Feedback();
            feedback.setUserId(userId);
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
} 
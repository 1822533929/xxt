package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Feedback;
import com.qjn.xiangxi_system.service.FeedbackService;
import com.qjn.xiangxi_system.utils.FileUploadUtil;
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
    public ResponseEntity<?> addFeedback(
            @RequestParam("image") MultipartFile image,
            @RequestParam("userId") Long userId,
            @RequestParam("type") String type,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "contact", required = false) String contact
    ) {
        try {
            // 上传图片
            String imageUrl = fileUploadUtil.uploadImage(image);
            
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
                return ResponseEntity.ok().body("反馈提交成功");
            } else {
                return ResponseEntity.badRequest().body("反馈提交失败");
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("图片上传失败：" + e.getMessage());
        }
    }
} 
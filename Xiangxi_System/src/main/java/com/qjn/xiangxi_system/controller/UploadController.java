package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.utils.FileUploadUtil;
import com.qjn.xiangxi_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @PostMapping("/image")
    public Result uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String url = fileUploadUtil.uploadImage(file);
            if (url == null) {
                return Result.error("图片上传失败");
            }
            System.out.println("Upload success, url: " + url);
            return Result.success(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }
} 
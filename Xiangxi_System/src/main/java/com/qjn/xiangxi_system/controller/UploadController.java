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

    /**
     * 富文本编辑器图片
     * @param file
     * @return
     */
    @PostMapping("/image")
    public Result uploadImageFromEdit(@RequestParam("image") MultipartFile file, @RequestParam("path") String path) {
        try {
            String url = fileUploadUtil.uploadImage(file,path);
            if (url == null) {
                return Result.error("图片上传失败");
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }
} 
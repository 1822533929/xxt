package com.qjn.xiangxi_system.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {
    
    /**
     * 上传图片文件
     * @param file 图片文件
     * @return 返回图片访问路径
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("只能上传图片文件");
        }

        // 获取项目中static目录的绝对路径
        String projectPath = ResourceUtils.getURL("classpath:").getPath();
        String uploadPath = projectPath + "static/img/upload";
        
        // 确保目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成新的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;
        
        // 保存文件
        File destFile = new File(uploadPath + File.separator + newFileName);
        file.transferTo(destFile);

        // 返回可访问的URL路径
        return "/img/upload/" + newFileName;
    }
} 
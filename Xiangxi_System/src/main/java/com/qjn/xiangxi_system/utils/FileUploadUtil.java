package com.qjn.xiangxi_system.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {
    
    @Value("${file.upload-path}")
    private String uploadPath;
    
    @PostConstruct
    public void init() {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (created) {
                System.out.println("Created upload directory: " + uploadDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create upload directory: " + uploadDir.getAbsolutePath());
            }
        }
    }
    
    /**
     * 上传图片文件
     * @param file 图片文件
     * @return 返回图片访问路径
     */
    public String uploadImage(MultipartFile file, String path) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        //使用自定义地址，分类图片文件夹
        String uploadPathTemp=uploadPath+"/"+path;
        System.out.println("自定义路径如下：Upload path: " + uploadPathTemp);
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("只能上传图片文件");
        }

        // 生成新的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;
        
        // 保存文件
        File uploadDir = new File(uploadPathTemp);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("无法创建上传目录：" + uploadDir.getAbsolutePath());
            }
        }
        
        // 使用绝对路径创建目标文件
        File destFile = new File(uploadDir, newFileName);
        
        System.out.println("Uploading file to: " + destFile.getAbsolutePath());
        try {
            file.transferTo(destFile);
            System.out.println("File uploaded successfully to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            throw e;
        }

        // 返回可访问的URL路径
        return "/img/upload/"+path+"/" + newFileName;
    }
    public String uploadImageFromEdit(MultipartFile file, String path) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        //使用自定义地址，分类图片文件夹
        String uploadPathTemp=uploadPath+"/"+path;
        System.out.println("自定义路径如下：Upload path: " + uploadPathTemp);
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("只能上传图片文件");
        }

        // 生成新的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;

        // 保存文件
        File uploadDir = new File(uploadPathTemp);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new IOException("无法创建上传目录：" + uploadDir.getAbsolutePath());
            }
        }

        // 使用绝对路径创建目标文件
        File destFile = new File(uploadDir, newFileName);

        System.out.println("Uploading file to: " + destFile.getAbsolutePath());
        try {
            file.transferTo(destFile);
            System.out.println("File uploaded successfully to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            throw e;
        }

        // 返回可访问的URL路径
        return "/img/upload/"+path+"/" + newFileName;
    }
} 
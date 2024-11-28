package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @RequestMapping("/login")
    public String login(String username,String password){
        return "login";
    }
}

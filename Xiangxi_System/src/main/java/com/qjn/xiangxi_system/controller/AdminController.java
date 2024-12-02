package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.Admin;
import com.qjn.xiangxi_system.service.AdminService;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @RequestMapping("/login")
    public Result login(Admin admin){
        System.out.println("AdminLogin:"+admin.getUsername()+","+admin.getPassword());
        if(adminService.login(admin)==1){//账号密码正确，进入管理员后台
            return Result.success();
        }else{
            return Result.error();
        }


    }
}

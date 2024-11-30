package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qjn.xiangxi_system.service.UserService;
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/login")
    public Result login(User user){
        System.out.println("UserLogin:"+user.getUsername()+","+user.getPassword());
        if(userService.login(user)==1){//账号密码正确，进入用户后台
            return Result.suc();
        }else{
            return Result.fail();
        }
    }
    @RequestMapping("/register")
    public Result register(User user){
        System.out.println("UserRegister:"+user.getUsername()+","+user.getPassword());
        if(userService.register(user)==1){//注册成功
            return Result.suc();
        }else{
            return Result.fail();
        }
    }

}

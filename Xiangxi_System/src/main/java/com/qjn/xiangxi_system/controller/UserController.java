package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qjn.xiangxi_system.service.UserService;
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public Result login(User user) {
        if(userService.login(user)==1){
            return Result.success();
        }else{
            return Result.error();
        }
    }

    @RequestMapping("/register")
    public Result register(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if(userService.register(user)==1){
            return Result.success();
        }else{
            return Result.error();
        }
    }
}

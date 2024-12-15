package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.qjn.xiangxi_system.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.qjn.xiangxi_system.utils.JSONUtils;
import com.qjn.xiangxi_system.utils.JWTUtils;
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/adminLogin")
    public Result adminLogin(@RequestParam("username") String username, 
                           @RequestParam("password") String password) {
        try {
            // 验证用户名和密码
            User user = userService.findByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 验证是否是管理员
            if (!"0".equals(user.getRole())) {
                return Result.error("非管理员账号");
            }
            
            // 验证密码
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(password, user.getPassword())) {
                return Result.error("密码错误");
            }

            // 生成 token
            String userJSON = JSONUtils.toJSON(user);
            String jwt = JWTUtils.createJWT(userJSON);
            
            return Result.success(jwt, "登录成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}

package com.qjn.xiangxi_system.controller;

import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.qjn.xiangxi_system.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.qjn.xiangxi_system.utils.JSONUtils;
import com.qjn.xiangxi_system.utils.JWTUtils;
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
            if (user.getRole()!=0) {
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
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("phone") String phone)
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
           return Result.success(userService.register(user));
    }
}

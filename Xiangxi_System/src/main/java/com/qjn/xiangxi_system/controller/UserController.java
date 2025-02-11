package com.qjn.xiangxi_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.pojo.query.UserQuery;
import com.qjn.xiangxi_system.utils.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.qjn.xiangxi_system.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileUploadUtil fileUploadUtil;

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
    /**
     * admin
     * 展示所有用户信息
     */
    @GetMapping("/selectAllUser")
    public Result<PageInfo<User>> selectAll(UserQuery query)
    {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<User> pageInfo = new PageInfo<>(userService.selectAllUser());
        return Result.success(pageInfo);
    }
    /**
     * admin
     * 展示所有管理员信息
     */
    @GetMapping("/selectAllAdmin")
    public Result<PageInfo<User>> selectAllAdmin(UserQuery query)
    {
        PageHelper.startPage(query.getCurrentPage(), query.getPageSize());
        PageInfo<User> pageInfo = new PageInfo<>(userService.selectAllAdmin());
        return Result.success(pageInfo);
    }
    /**
     * admin
     * 添加用户
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Result.success(userService.save(user));
    }

    /**
     * admin
     * 修改用户信息
     */
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user)
    {
        return Result.success(userService.updateById(user));
    }
    /**
     * admin
     * 删除用户
     */
    @GetMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable Integer id)
    {
        return Result.success(userService.removeById(id));
    }
    /**
     * admin
     * 批量删除用户
     */
    @PostMapping("/deleteUser/batch")
    public Result batchDeleteUser(@RequestBody List<Integer> ids)
    {
        userService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 获取用户名和头像
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        //获取用户信息
        User user = UserToken.parseUserFromToken(token);
        if (user == null || user.getId() == null) {
            return Result.error("无法获取用户信息");
        }
        // 确保返回的用户信息是最新的
        user = userService.getById(user.getId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 如果头像路径不为空，确保路径格式正确
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            String avatarPath = user.getAvatar().replace("/img/upload/", "");
            user.setAvatar(avatarPath);
        }

        return Result.success(user);
    }
    /**
     * 个人信息更改
     *
     */
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(
        @RequestParam("username") String username,
        @RequestParam("name") String name,
        @RequestParam("phone") String phone,
        @RequestParam("email") String email,
        @RequestParam(value = "image", required = false) MultipartFile image,
        @RequestHeader("Authorization") String token
    ) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setPhone(phone);
            user.setEmail(email);
            
            String avatar = null;
            // 只在有图片且不是空文件时处理图片上传
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                avatar = fileUploadUtil.uploadImage(image, "user");
                user.setAvatar(avatar);
            }

            //获取用户信息
            User user1 = UserToken.parseUserFromToken(token);
            if (user1 == null || user1.getId() == null) {
                return Result.error("无法获取用户信息");
            }
            user.setId(user1.getId());
            userService.updateById(user);
            return Result.success();
        } catch (IOException e) {
            return Result.error("图片上传失败");
        }
    }
    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(String oldPassword,String newPassword,@RequestHeader("Authorization") String token){
        //获取用户信息
        User user = UserToken.parseUserFromToken(token);
        if (user == null || user.getId() == null) {
            return Result.error("无法获取用户信息");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateById(user);
        return Result.success();
    }
}

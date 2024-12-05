package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Admin;
import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.service.UserService;
import com.qjn.xiangxi_system.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-30 19:29:37
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;

//    @Override
//    public int login(User user) {
//        User user1 = userMapper.isExist(user.getUsername());
//        if (user1 == null) {
//            System.out.println("登录失败：用户不存在");
//            return 0;
//        } else if (!user.getPassword().equals(user1.getPassword())) {
//            System.out.println("登录失败：密码错误");
//            return 0;
//        }
//        System.out.println("登录成功");
//        return 1;
//    }

    @Override
    public int register(User user) {
        User user1 = userMapper.isExist(user.getUsername());//验证账号是否重复
        if (user1 != null) {
            System.out.println("注册失败：用户已存在");
            return 0;
        }else{
            userMapper.insert(user);//添加用户
            return 1;
        }
    }
}





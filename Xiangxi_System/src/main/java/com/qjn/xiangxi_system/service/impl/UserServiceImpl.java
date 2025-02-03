package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Admin;
import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.service.UserService;
import com.qjn.xiangxi_system.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-30 19:29:37
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        userMapper.deleteBatch(ids);
    }

    @Override
    public List<User> selectAllAdmin() {
        return userMapper.selectAllAdmin();
    }

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





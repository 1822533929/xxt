package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-30 19:29:37
*/
public interface UserService extends IService<User> {

//    int login(User user);

    int register(User user);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    List<User> selectAllUser();

    void deleteBatch(List<Integer> ids);

    List<User> selectAllAdmin();
}

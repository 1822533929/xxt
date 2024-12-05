package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-11-30 19:29:37
* @Entity com.qjn.xiangxi_system.pojo.User
*/
public interface UserMapper extends BaseMapper<User> {
    User isExist(String username);
    User getUserByUsername(String username);

    User selectByUsername(String username);
}





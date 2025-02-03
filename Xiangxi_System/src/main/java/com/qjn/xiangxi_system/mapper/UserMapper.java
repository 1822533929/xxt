package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-11-30 19:29:37
* @Entity com.qjn.xiangxi_system.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User isExist(String username);
    User getUserByUsername(String username);

    User selectByUsername(String username);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    List<User> selectAllUser();

    void deleteBatch(List<Integer> ids);

    List<User> selectAllAdmin();
}





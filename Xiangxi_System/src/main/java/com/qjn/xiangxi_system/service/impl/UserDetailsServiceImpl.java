package com.qjn.xiangxi_system.service.impl;

import com.qjn.xiangxi_system.mapper.UserMapper;
import com.qjn.xiangxi_system.pojo.User;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("正在查询用户：{}", username);
        
        // 从数据库中查询用户
        User user = userMapper.selectByUsername(username);
        
        if (user == null) {
            log.error("用户不存在：{}", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        
        log.info("查询到的用户信息：{}", user);
        return user;
    }
}
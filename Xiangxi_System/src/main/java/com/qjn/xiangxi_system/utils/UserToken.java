package com.qjn.xiangxi_system.utils;

import com.qjn.xiangxi_system.pojo.User;

/**
 * 从前端token读取用户信息
 * @author qjn
 */
public class UserToken {
    public static User parseUserFromToken(String token){
        // 移除 Bearer 前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 token
        if (!JWTUtils.verifyJWT(token)) {
            return null;
        }

        // 解析 token 获取用户信息
        User user = JWTUtils.parseUserFromJWT(token);
        return user;
    }
}

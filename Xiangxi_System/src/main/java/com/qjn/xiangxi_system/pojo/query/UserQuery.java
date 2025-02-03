package com.qjn.xiangxi_system.pojo.query;

import lombok.Data;

@Data
public class UserQuery extends BaseQuery{
    /**
     * ID
     */
    private Integer id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}

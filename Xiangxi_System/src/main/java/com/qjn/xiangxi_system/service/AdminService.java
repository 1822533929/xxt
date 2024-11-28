package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qjn.xiangxi_system.utils.Result;

/**
* @author Administrator
* @description 针对表【admin】的数据库操作Service
* @createDate 2024-11-28 20:04:56
*/
public interface AdminService extends IService<Admin> {
    int login(Admin admin);

}

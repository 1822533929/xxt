package com.qjn.xiangxi_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjn.xiangxi_system.pojo.Admin;
import com.qjn.xiangxi_system.service.AdminService;
import com.qjn.xiangxi_system.mapper.AdminMapper;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【admin】的数据库操作Service实现
* @createDate 2024-11-28 20:04:56
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{
    @Resource
    private AdminMapper adminMapper;

    @Override
    public int login(Admin admin) {
        Admin admin1 = adminMapper.isExist(admin.getUsername());
        if (admin1 == null) {
            System.out.println("登录失败：用户不存在");
            return 0;
        } else if (!admin.getPassword().equals(admin1.getPassword())) {
            System.out.println("登录失败：密码错误");
            return 0;
        }
        System.out.println("登录成功");
        return 1;
    }
}





package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Travels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【travels】的数据库操作Mapper
* @createDate 2024-12-25 20:33:50
* @Entity com.qjn.xiangxi_system.pojo.Travels
*/
public interface TravelsMapper extends BaseMapper<Travels> {

    List<Travels> selectAllByRead();

    void deleteBatch(List<Integer> ids);
}





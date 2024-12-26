package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Travels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelsMapper extends BaseMapper<Travels> {

    List<Travels> selectAllByRead();

    void deleteBatch(List<Integer> ids);

    List<String> selectTags(Integer id);
}





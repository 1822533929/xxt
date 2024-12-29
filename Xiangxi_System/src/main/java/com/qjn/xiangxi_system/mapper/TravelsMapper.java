package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Travels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjn.xiangxi_system.pojo.vo.TravelsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TravelsMapper extends BaseMapper<Travels> {


    List<Travels> selectAllByRead();

    void deleteBatch(List<Integer> ids);

    List<String> selectTags(Integer id);

    List<TravelsVO> SelectAll();

    void insertTravelTags(@Param("travelId") Integer travelId, @Param("tagIds") List<Integer> tagIds);

    Integer getTagIdByName(@Param("tagName") String tagName);

    void insertTag(@Param("tagName") String tagName);

    void deleteTravelTags(@Param("travelId") Integer travelId);

    void deleteBatchTravelTags(@Param("travelIds") List<Integer> travelIds);
}





package com.qjn.xiangxi_system.mapper;

import com.qjn.xiangxi_system.pojo.Travels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjn.xiangxi_system.pojo.vo.TravelsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TravelsMapper extends BaseMapper<Travels> {


    List<TravelsVO> selectAllByRead();

    void deleteBatch(List<Integer> ids);

    List<String> selectTags(Integer id);

    List<TravelsVO> SelectAll();

    void insertTravelTags(@Param("travelId") Integer travelId, @Param("tagIds") List<Integer> tagIds);

    Integer getTagIdByName(@Param("tagName") String tagName);

    void insertTag(@Param("tagName") String tagName);

    void deleteTravelTags(@Param("travelId") Integer travelId);

    void deleteBatchTravelTags(@Param("travelIds") List<Integer> travelIds);

    /**
     * 模糊查询所有旅游商品及其标签
     */
    List<TravelsVO> selectAllWithTags(@Param("keyword") String keyword);

    Integer getInventory(Integer travelId);

    void reduceInventory(Integer travelId, Integer quantity);

    void releaseInventory(Integer travelid, Integer quantity);

    @Select("SELECT t.title, COUNT(o.id) as sales " +
            "FROM travels t " +
            "LEFT JOIN orders o ON t.id = o.travelid " +
            "GROUP BY t.id " +
            "ORDER BY sales DESC " +
            "LIMIT 5")
    List<Map<String, Object>> getProductRank();
}





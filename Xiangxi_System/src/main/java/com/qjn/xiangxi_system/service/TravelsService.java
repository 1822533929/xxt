package com.qjn.xiangxi_system.service;

import com.qjn.xiangxi_system.pojo.Travels;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qjn.xiangxi_system.pojo.vo.TravelsVO;
import java.util.List;

/**
* @author Administrator
* @description 针对表【travels】的数据库操作Service
* @createDate 2024-12-25 20:33:50
*/
public interface TravelsService extends IService<Travels> {

    List<Travels> selectAllByRead();

    void deleteBatch(List<Integer> ids);

    List<String> selectTags(Integer id);

    List<TravelsVO> SelectAll();

    boolean saveWithTags(TravelsVO travelsVO);
}

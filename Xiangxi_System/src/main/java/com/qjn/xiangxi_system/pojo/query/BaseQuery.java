package com.qjn.xiangxi_system.pojo.query;

import lombok.Data;



@Data
public class BaseQuery {
    private Integer currentPage =  1;
    private Integer pageSize = 10;
}

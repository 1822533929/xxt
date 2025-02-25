package com.qjn.xiangxi_system.pojo.vo;

import com.qjn.xiangxi_system.pojo.Comments;
import lombok.Data;

@Data
public class CommentsVO extends Comments {
    private String name;
    private String avatar;
    private String date;
}

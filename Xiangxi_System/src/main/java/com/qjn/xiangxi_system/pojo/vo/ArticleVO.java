package com.qjn.xiangxi_system.pojo.vo;

import com.qjn.xiangxi_system.pojo.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleVO extends Article {
    private String name;
    private String avatar;
} 
package com.qjn.xiangxi_system.pojo.query;

/**
 * 
 * @TableName article
 */
public class ArticleQuery extends BaseQuery{
    /**
     * ID
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String descr;

    /**
     * 封面
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞量
     */
    private Integer likes;

    /**
     * 发布日期
     */
    private String date;

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 描述
     */
    public String getDescr() {
        return descr;
    }

    /**
     * 描述
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * 封面
     */
    public String getCover() {
        return cover;
    }

    /**
     * 封面
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 点赞量
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * 点赞量
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * 发布日期
     */
    public String getDate() {
        return date;
    }

    /**
     * 发布日期
     */
    public void setDate(String date) {
        this.date = date;
    }


}
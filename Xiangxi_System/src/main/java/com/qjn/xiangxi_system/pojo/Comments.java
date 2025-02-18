package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;

/**
 * 存储文章的评论信息
 * @TableName comments
 */
public class Comments implements Serializable {
    /**
     * 评论ID，主键，自增
     */
    private Integer id;

    /**
     * 文章ID，外键关联文章表
     */
    private Integer articleId;

    /**
     * 用户ID，外键关联用户表
     */
    private Integer userId;

    /**
     * 多级评论，null为顶级评论
     */
    private Integer parentId;

    /**
     * 点赞量
     */
    private Integer likes;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createdAt;

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID，主键，自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 评论ID，主键，自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 文章ID，外键关联文章表
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * 文章ID，外键关联文章表
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    /**
     * 用户ID，外键关联用户表
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 用户ID，外键关联用户表
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 多级评论，null为顶级评论
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 多级评论，null为顶级评论
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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
     * 评论内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 评论内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 创建时间
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 创建时间
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Comments other = (Comments) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getArticleId() == null ? other.getArticleId() == null : this.getArticleId().equals(other.getArticleId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getLikes() == null ? other.getLikes() == null : this.getLikes().equals(other.getLikes()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getArticleId() == null) ? 0 : getArticleId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getLikes() == null) ? 0 : getLikes().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", articleId=").append(articleId);
        sb.append(", userId=").append(userId);
        sb.append(", parentId=").append(parentId);
        sb.append(", likes=").append(likes);
        sb.append(", content=").append(content);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
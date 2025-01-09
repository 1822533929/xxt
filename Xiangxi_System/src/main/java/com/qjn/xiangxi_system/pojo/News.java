package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;

/**
 * 资讯数据表
 * @TableName news
 */
public class News implements Serializable {
    /**
     * 资讯唯一标识符
     */
    private Integer id;

    /**
     * 资讯标题
     */
    private String title;

    /**
     * 封面图片URL
     */
    private String cover;

    /**
     * 资讯摘要
     */
    private String summary;

    /**
     * 资讯内容
     */
    private String content;

    /**
     * 发布时间
     */
    private String publishTime;

    /**
     * 浏览量
     */
    private Integer readCount;

    private static final long serialVersionUID = 1L;

    /**
     * 资讯唯一标识符
     */
    public Integer getId() {
        return id;
    }

    /**
     * 资讯唯一标识符
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 资讯标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 资讯标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 封面图片URL
     */
    public String getCover() {
        return cover;
    }

    /**
     * 封面图片URL
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * 资讯摘要
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 资讯摘要
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 资讯内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 资讯内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 发布时间
     */
    public String getPublishTime() {
        return publishTime;
    }

    /**
     * 发布时间
     */
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 浏览量
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * 浏览量
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
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
        News other = (News) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getPublishTime() == null ? other.getPublishTime() == null : this.getPublishTime().equals(other.getPublishTime()))
            && (this.getReadCount() == null ? other.getReadCount() == null : this.getReadCount().equals(other.getReadCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getPublishTime() == null) ? 0 : getPublishTime().hashCode());
        result = prime * result + ((getReadCount() == null) ? 0 : getReadCount().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", cover=").append(cover);
        sb.append(", summary=").append(summary);
        sb.append(", content=").append(content);
        sb.append(", publishTime=").append(publishTime);
        sb.append(", readCount=").append(readCount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;

/**
 * 
 * @TableName article
 */
public class Article implements Serializable {
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
     * 阅读量
     */
    private Integer readCount;

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
     * 阅读量
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * 阅读量
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
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
        Article other = (Article) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getDescr() == null ? other.getDescr() == null : this.getDescr().equals(other.getDescr()))
            && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getReadCount() == null ? other.getReadCount() == null : this.getReadCount().equals(other.getReadCount()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getDescr() == null) ? 0 : getDescr().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getReadCount() == null) ? 0 : getReadCount().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
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
        sb.append(", descr=").append(descr);
        sb.append(", cover=").append(cover);
        sb.append(", content=").append(content);
        sb.append(", readCount=").append(readCount);
        sb.append(", date=").append(date);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
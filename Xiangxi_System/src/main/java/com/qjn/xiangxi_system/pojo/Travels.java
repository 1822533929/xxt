package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName travels
 */
public class Travels implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 简介
     */
    private String descr;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private String time;

    /**
     * 阅读量
     */
    private Integer readCount;



    /**
     * 费用
     */
    private BigDecimal money;

    /**
     * 库存
     */
    private Integer inventory;
    /**
     * 点赞量
     */
    private Integer likeCount;

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
     * 简介
     */
    public String getDescr() {
        return descr;
    }

    /**
     * 简介
     */
    public void setDescr(String descr) {
        this.descr = descr;
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
     * 发布时间
     */
    public String getTime() {
        return time;
    }

    /**
     * 发布时间
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * 
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }



    /**
     * 费用
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 费用
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 库存
     */
    public Integer getInventory() {
        return inventory;
    }

    /**
     * 库存
     */
    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    /**
     * 点赞量
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * 点赞量
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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
        Travels other = (Travels) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
            && (this.getDescr() == null ? other.getDescr() == null : this.getDescr().equals(other.getDescr()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
            && (this.getReadCount() == null ? other.getReadCount() == null : this.getReadCount().equals(other.getReadCount()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getInventory() == null ? other.getInventory() == null : this.getInventory().equals(other.getInventory())
                && (this.getLikeCount() == null ? other.getLikeCount() == null : this.getLikeCount().equals(other.getLikeCount())));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getDescr() == null) ? 0 : getDescr().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        result = prime * result + ((getReadCount() == null) ? 0 : getReadCount().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getInventory() == null) ? 0 : getInventory().hashCode());
        result = prime * result + ((getLikeCount() == null) ? 0 : getLikeCount().hashCode());
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
        sb.append(", descr=").append(descr);
        sb.append(", content=").append(content);
        sb.append(", time=").append(time);
        sb.append(", readCount=").append(readCount);
        sb.append(", money=").append(money);
        sb.append(", inventory=").append(inventory);
        sb.append(", likeCount=").append(likeCount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
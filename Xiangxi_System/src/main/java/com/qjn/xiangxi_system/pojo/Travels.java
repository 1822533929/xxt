package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;

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
     * 
     */
    private Integer readCount;

    /**
     * 出行日期
     */
    private String startDate;

    /**
     * 费用
     */
    private String money;

    /**
     * 
     */
    private Integer inventory;

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
     * 出行日期
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 出行日期
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 费用
     */
    public String getMoney() {
        return money;
    }

    /**
     * 费用
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * 
     */
    public Integer getInventory() {
        return inventory;
    }

    /**
     * 
     */
    public void setInventory(Integer inventory) {
        this.inventory = inventory;
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
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()))
            && (this.getInventory() == null ? other.getInventory() == null : this.getInventory().equals(other.getInventory()));
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
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        result = prime * result + ((getInventory() == null) ? 0 : getInventory().hashCode());
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
        sb.append(", startDate=").append(startDate);
        sb.append(", money=").append(money);
        sb.append(", inventory=").append(inventory);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
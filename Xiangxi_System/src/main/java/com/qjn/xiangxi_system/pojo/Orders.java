package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 * @TableName orders
 */
public class Orders implements Serializable {
    /**
     * 订单ID，主键
     */
    private Integer id;

    /**
     * 用户ID，外键关联User表
     */
    private Integer userid;

    /**
     * 旅行ID，外键关联Travels表
     */
    private Integer travelid;

    /**
     * 订单日期
     */
    private Date orderdate;

    /**
     * 订单状态（如：待支付、已支付、已取消等）
     */
    private String status;

    /**
     * 订单总价
     */
    private BigDecimal totalprice;

    /**
     * 购买的旅行套餐数量
     */
    private Integer quantity;

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID，主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 订单ID，主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户ID，外键关联User表
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 用户ID，外键关联User表
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 旅行ID，外键关联Travels表
     */
    public Integer getTravelid() {
        return travelid;
    }

    /**
     * 旅行ID，外键关联Travels表
     */
    public void setTravelid(Integer travelid) {
        this.travelid = travelid;
    }



    /**
     * 订单日期
     */
    public Date getOrderdate() {
        return orderdate;
    }

    /**
     * 订单日期
     */
    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    /**
     * 订单状态（如：待支付、已支付、已取消等）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 订单状态（如：待支付、已支付、已取消等）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 订单总价
     */
    public BigDecimal getTotalprice() {
        return totalprice;
    }

    /**
     * 订单总价
     */
    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    /**
     * 购买的旅行套餐数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 购买的旅行套餐数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
        Orders other = (Orders) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getTravelid() == null ? other.getTravelid() == null : this.getTravelid().equals(other.getTravelid()))
            && (this.getOrderdate() == null ? other.getOrderdate() == null : this.getOrderdate().equals(other.getOrderdate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getTotalprice() == null ? other.getTotalprice() == null : this.getTotalprice().equals(other.getTotalprice()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getTravelid() == null) ? 0 : getTravelid().hashCode());
        result = prime * result + ((getOrderdate() == null) ? 0 : getOrderdate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTotalprice() == null) ? 0 : getTotalprice().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", travelid=").append(travelid);
        sb.append(", orderdate=").append(orderdate);
        sb.append(", status=").append(status);
        sb.append(", totalprice=").append(totalprice);
        sb.append(", quantity=").append(quantity);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
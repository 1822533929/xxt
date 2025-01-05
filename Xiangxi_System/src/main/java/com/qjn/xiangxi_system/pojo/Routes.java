package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;

/**
 * 
 * @TableName routes
 */
public class Routes implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 小提示
     */
    private String tips;

    /**
     * 路线内容
     */
    private String content;

    /**
     * 图片
     */
    private String img;

    /**
     * 地点
     */
    private String location;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;

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
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 天数
     */
    public Integer getDays() {
        return days;
    }

    /**
     * 天数
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * 小提示
     */
    public String getTips() {
        return tips;
    }

    /**
     * 小提示
     */
    public void setTips(String tips) {
        this.tips = tips;
    }

    /**
     * 路线内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 路线内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 图片
     */
    public String getImg() {
        return img;
    }

    /**
     * 图片
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 地点
     */
    public String getLocation() {
        return location;
    }

    /**
     * 地点
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 维度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 维度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
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
        Routes other = (Routes) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDays() == null ? other.getDays() == null : this.getDays().equals(other.getDays()))
            && (this.getTips() == null ? other.getTips() == null : this.getTips().equals(other.getTips()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
            && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()))
            && (this.getLongitude() == null ? other.getLongitude() == null : this.getLongitude().equals(other.getLongitude()))
            && (this.getLatitude() == null ? other.getLatitude() == null : this.getLatitude().equals(other.getLatitude()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDays() == null) ? 0 : getDays().hashCode());
        result = prime * result + ((getTips() == null) ? 0 : getTips().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getLongitude() == null) ? 0 : getLongitude().hashCode());
        result = prime * result + ((getLatitude() == null) ? 0 : getLatitude().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", days=").append(days);
        sb.append(", tips=").append(tips);
        sb.append(", content=").append(content);
        sb.append(", img=").append(img);
        sb.append(", location=").append(location);
        sb.append(", longitude=").append(longitude);
        sb.append(", latitude=").append(latitude);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
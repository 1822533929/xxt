package com.qjn.xiangxi_system.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 问题反馈表
 * @TableName feedback
 */
public class Feedback implements Serializable {
    /**
     * 反馈ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 反馈类型：suggestion-功能建议，bug-问题报告，complaint-投诉，other-其他
     */
    private String type;

    /**
     * 反馈标题
     */
    private String title;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 处理状态：pending-待处理，completed-已完成
     */
    private String status;

    /**
     * 回复内容
     */
    private String reply;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    /**
     * 反馈ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 反馈ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 反馈类型：suggestion-功能建议，bug-问题报告，complaint-投诉，other-其他
     */
    public String getType() {
        return type;
    }

    /**
     * 反馈类型：suggestion-功能建议，bug-问题报告，complaint-投诉，other-其他
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 反馈标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 反馈标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 图片URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 图片URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 反馈内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 联系方式
     */
    public String getContact() {
        return contact;
    }

    /**
     * 联系方式
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 处理状态：pending-待处理，completed-已完成
     */
    public String getStatus() {
        return status;
    }

    /**
     * 处理状态：pending-待处理，completed-已完成
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 回复内容
     */
    public String getReply() {
        return reply;
    }

    /**
     * 回复内容
     */
    public void setReply(String reply) {
        this.reply = reply;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
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
        Feedback other = (Feedback) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getImageUrl() == null ? other.getImageUrl() == null : this.getImageUrl().equals(other.getImageUrl()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getContact() == null ? other.getContact() == null : this.getContact().equals(other.getContact()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getReply() == null ? other.getReply() == null : this.getReply().equals(other.getReply()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getImageUrl() == null) ? 0 : getImageUrl().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getContact() == null) ? 0 : getContact().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getReply() == null) ? 0 : getReply().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", type=").append(type);
        sb.append(", title=").append(title);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", content=").append(content);
        sb.append(", contact=").append(contact);
        sb.append(", status=").append(status);
        sb.append(", reply=").append(reply);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
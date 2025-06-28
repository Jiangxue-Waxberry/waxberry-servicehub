package com.jiangxue.waxberry.manager.industrialPrompt.dto;


import java.util.Date;


public class IndustrialPromptDto {


    private String id;

    private String title;

    private String content;
    private String status;

    private String creatorId;

    private String creatorName;

    private String updaterId;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public IndustrialPromptDto(String id,
                               String title,
                               String content,
                               String status,
                               String creatorId,
                               String creatorName,
                               String updaterId,
                               Date createTime,
                               Date updateTime){
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.creatorId = creatorId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.updaterId = updaterId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}

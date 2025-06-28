package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_agent_agent")
@Validated
public class Agent implements Serializable {

    private static final long serialVersionUID = 1116787341637611441L;

    @Id
    @Column(length = 8)
    @GeneratedValue(generator = "waxberry-id")
    @GenericGenerator(
            name = "waxberry-id",
            strategy = "com.jiangxue.waxberry.manager.agent.util.MyAgentIdGenerator"
    )
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "agent_label")
    private String agentLabel;

    @Column(name = "discription",columnDefinition = "TEXT")
    private String discription;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "step")
    private String step;

    @Column(name = "creator_id")
    private String creatorId;

    @Column(name = "create_time")
    private Date createTime = new Date();

    @Column(name = "update_time")
    private Date updateTime = new Date();

    @Column(name = "classification_id")
    private String classificationId = "0";

    @Column(name="ismodify")
    private Integer ismodify = 1;

    //是否允许复制 0: 允许 1：不允许
    @Column(name="is_copy")
    private Integer isCopy = 0;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "imge_file_id")
    private String imgeFileId;

    @Column(name = "vessel_id")
    private String vesselId;

    @Column(name = "cover_file_id")
    private String coverFileId;

    @Column(name = "vessel_port")
    private Integer vesselPort;

    @Column(name = "type")
    private Integer type;

    public String getVesselId() {
        return vesselId;
    }

    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }

    public String getCoverFileId() {
        return coverFileId;
    }

    public void setCoverFileId(String coverFileId) {
        this.coverFileId = coverFileId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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



    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public Integer getIsmodify() {
        return ismodify;
    }

    public void setIsmodify(Integer ismodify) {
        this.ismodify = ismodify;
    }

    public String getAgentLabel() {
        return agentLabel;
    }

    public void setAgentLabel(String agentLabel) {
        this.agentLabel = agentLabel;
    }



    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getImgeFileId() {
        return imgeFileId;
    }

    public void setImgeFileId(String imgeFileId) {
        this.imgeFileId = imgeFileId;
    }

    public Integer getVesselPort() {
        return vesselPort;
    }

    public void setVesselPort(Integer vesselPort) {
        this.vesselPort = vesselPort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}

package com.jiangxue.waxberry.manager.agent.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;


@Data
@Validated
public class AgentDTO implements Serializable {

    private static final long serialVersionUID = 1116787341637611441L;


    private String id;

    private String name;

    private String agentLabel;

    private String discription;

    private Integer status = 0;

    private String creatorId;

    private Date createTime;

    private Date updateTime;

    private String classificationId;

    private Integer ismodify;

    private Integer isCopy;

    private Integer sortOrder;

    private String fileId;

    private String fileName;

    private String groupId;

    private String imgeFileId;

    private String vesselId;

    private String coverFileId;

    private String creatorName;

    private Integer vesselPort;

    private Long likeCount;

    private Long collectCount;

    private Long isLike;

    private Long isCollect;

    private Integer type;

    private Long runCount;

    private String step;

    public AgentDTO(String id, String name, String agentLabel, String discription, Integer status, String creatorId, Date createTime, Date updateTime, String classificationId, Integer ismodify, Integer sortOrder, String fileId, String fileName, String groupId, String imgeFileId, String vesselId, String coverFileId, String creatorName, Integer vesselPort, Integer type, String step, Integer isCopy) {
        this.id = id;
        this.name = name;
        this.agentLabel = agentLabel;
        this.discription = discription;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.classificationId = classificationId;
        this.ismodify = ismodify;
        this.sortOrder = sortOrder;
        this.fileId = fileId;
        this.fileName = fileName;
        this.groupId = groupId;
        this.imgeFileId = imgeFileId;
        this.creatorName = creatorName;
        this.vesselId = vesselId;
        this.coverFileId = coverFileId;
        this.vesselPort = vesselPort;
        this.type = type;
        this.step = step;
        this.isCopy = isCopy;
    }

    public AgentDTO(String id, String name, String agentLabel, String discription, Integer status, String creatorId, Date createTime, Date updateTime, String classificationId, Integer ismodify, Integer sortOrder, String fileId, String fileName, String groupId, String imgeFileId, String vesselId, String coverFileId, String creatorName, Integer vesselPort, Integer type) {
        this.id = id;
        this.name = name;
        this.agentLabel = agentLabel;
        this.discription = discription;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.classificationId = classificationId;
        this.ismodify = ismodify;
        this.sortOrder = sortOrder;
        this.fileId = fileId;
        this.fileName = fileName;
        this.groupId = groupId;
        this.imgeFileId = imgeFileId;
        this.creatorName = creatorName;
        this.vesselId = vesselId;
        this.coverFileId = coverFileId;
        this.vesselPort = vesselPort;
        this.type = type;
    }

    public AgentDTO(
            String id,
            String name,
            String agentLabel,
            String discription,
            Integer status,
            String creatorId,
            Date createTime,
            Date updateTime,
            String classificationId,
            Integer ismodify,
            Integer sortOrder,
            String fileId,
            String fileName,
            String groupId,
            String imgeFileId,
            String vesselId,
            String coverFileId,
            String creatorName,
            Integer vesselPort,
            Integer type,
            Long likeCount,
            Long collectCount,
            Long isLike,
            Long isCollect,
            Long runCount
    ) {
        this.id = id;
        this.name = name;
        this.agentLabel = agentLabel;
        this.discription = discription;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.classificationId = classificationId;
        this.ismodify = ismodify;
        this.sortOrder = sortOrder;
        this.fileId = fileId;
        this.fileName = fileName;
        this.groupId = groupId;
        this.imgeFileId = imgeFileId;
        this.creatorName = creatorName;
        this.vesselId = vesselId;
        this.coverFileId = coverFileId;
        this.vesselPort = vesselPort;
        this.type = type;
        this.likeCount = likeCount;
        this.collectCount = collectCount;
        this.isLike = isLike;
        this.isCollect = isCollect;
        this.runCount = runCount;
    }
}

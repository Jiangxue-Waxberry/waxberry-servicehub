package com.jiangxue.waxberry.manager.vessel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;

/**
 * VesselConversationRelation
 * @Description : 容器与会话关系表
 * @Author hgy
 * @Date 17:00 2025/5/6
 * @Version 1.0
 */
@Data
@Entity
@Table(name="mgr_vessel_conversationrelation")
@Validated
public class VesselConversationRelation implements Serializable {

    @Id
    @Column
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "vessel_id")
    private String vesselId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "conversation_name")
    private String conversationName;

    @Column(name = "creator")
    private String creator;

    @Column(name = "modifier")
    private String modifier;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

}

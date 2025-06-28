package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_agent_userrelation")
@Validated
public class AgentUserRelation implements Serializable {

    @Id
    @Column
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "agent_id")
    private String agentId;

    @Column(name = "vessel_id")
    private String vesselId;

    @Column(name = "vessel_port")
    private Integer vesselPort;

    @Column(name = "creator")
    private String creator;

    @Column(name = "modifier")
    private String modifier;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

}

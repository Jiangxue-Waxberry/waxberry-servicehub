package com.jiangxue.waxberry.manager.agent.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name="mgr_agent_approval")
@Validated
public class AgentApproval implements Serializable {

    private static final long serialVersionUID = 1116787341637611441L;

    @Id
    @Column
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;


    @Column(name = "agent_id")
    private String agentId;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "approval_code")
    private String approvalCode;

    @Column(name = "creator")
    private String creator;

    @Column(name = "approval_language")
    private String approvalLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status = Status.PROCESS;


    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;


    public enum Status {
        PROCESS, PASS, REFUSE
    }

    @PrePersist
    protected void onCreate() {
        this.createTime = Timestamp.valueOf(LocalDateTime.now());
        this.updateTime = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = Timestamp.valueOf(LocalDateTime.now());
    }


}

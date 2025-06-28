package com.jiangxue.waxberry.manager.redemptionCode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mgr_redemption_code")
public class RedemptionCode implements Serializable {

    @Id
    @Column(name = "id", length = 36, nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "user_id", length = 40)
    private String userId;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "status")
    private int status;

    @Column(name = "create_time", columnDefinition = "DATETIME")
    private Timestamp createTime;

    @Column(name = "modify_time", columnDefinition = "DATETIME")
    private Timestamp modifyTime;

    @Column(name = "deadline_time", columnDefinition = "DATETIME")
    private Timestamp deadlineTime;


}
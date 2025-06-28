package com.jiangxue.waxberry.manager.baseMiniMode.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="mgr_baseminmodel_model")
@Validated
public class BaseMiniModel {

    @Id
    @Column
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    //基模类型（视觉模型/数据模型）
    @Column(name = "type")
    private String type;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "creator_id")
    private String creatorId;

    @Column(name = "updater_id")
    private String updaterId;

    @Column(name = "create_time")
    private Date createTime = new Date();

    @Column(name = "update_time")
    private Date updateTime = new Date();


}

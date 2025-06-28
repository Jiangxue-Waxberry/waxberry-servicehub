package com.jiangxue.waxberry.manager.baseMiniMode.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="mgr_baseminmodel_extend")
@Validated
public class BaseMiniModelExtend {

    @Id
    @Column
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "waxberry_id")
    private String waxberryId;

    @Column(name = "name")
    private String name;

    //基模类型（视觉模型/数据模型）
    @Column(name = "type")
    private String type;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "creator_id")
    private String creatorId;

    @Column(name = "updater_id")
    private String updaterId;

    @Column(name = "create_time")
    private Date createTime = new Date();

    @Column(name = "update_time")
    private Date updateTime = new Date();


}

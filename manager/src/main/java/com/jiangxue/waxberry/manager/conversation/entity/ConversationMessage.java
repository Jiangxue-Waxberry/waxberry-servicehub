package com.jiangxue.waxberry.manager.conversation.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_conversation_message")
@Validated
public class ConversationMessage implements Serializable {

    @Id
    @Column
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiangxue.framework.common.util.CustomUUIDGenerator")
    private String id;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "kb_id")
    private String kbId;

    @Column(name = "parent_id")
    @NotEmpty
    private String parentId;

    @Column(name = "extend")
    private String extend;

    @Column(name = "query",columnDefinition = "LONGTEXT")
    @NotEmpty
    private String query;

    @Column(name = "reponse",columnDefinition = "LONGTEXT")
    private String reponse;

    @Column(name = "reponse_file_id")
    private String reponseFileId;

    @Column(name = "meta_data")
    private String metaData;

    @Column(name = "feedback_score")
    private String feedbackScore;

    @Column(name = "feedback_reason")
    private String feedbackReason;

    @Column(name = "status")
    private String status;

    @Column(name = "creator")
    private String creator;

    @Column(name = "modifier")
    private String modifier;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

}

package com.jiangxue.waxberry.manager.conversation.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name="mgr_conversation_conversation")
@Validated
public class Conversation implements Serializable {

    @Id
    @Column
    private String id;

    @Column(name = "title", length = 4000)
    @NotEmpty
    private String title;

    @Column(name = "CHAT_TYPE")
    private String chatType;

    @Column()
    private String status;

    @Column
    private String creator;

    @Column
    private String modifier;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "MODIFY_TIME")
    private Date modifyTime;




}

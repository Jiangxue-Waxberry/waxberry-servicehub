package com.jiangxue.waxberry.manager.conversation.dto;

import lombok.Data;

import java.util.Date;


@Data
public class ConversationDto {

    private String id;
    private String title;
    private String chatType;
    private String status;
    private String creator;
    private String creatorName;
    private String modifier;
    private String modifierName;
    private Date createTime;
    private Date modifyTime;

}

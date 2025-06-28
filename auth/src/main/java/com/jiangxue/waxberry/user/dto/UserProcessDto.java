package com.jiangxue.waxberry.user.dto;

import com.jiangxue.waxberry.user.entity.UserProcess;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserProcessDto {

    private String id;
    private String mobile;
    private UserProcess.ProcessStatus status;
    private String adminUser;
    private String approvalNumber;
    private String approvalLanguage;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

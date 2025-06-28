package com.jiangxue.waxberry.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApprovalDto {

    private String id;

    private String approvalCode;

    private String username;

    private String mobile;

    private String userType;

    private Date approvalCreateTime;

    private String approvalStatus;

    private Integer hour;

    private String userId;

}

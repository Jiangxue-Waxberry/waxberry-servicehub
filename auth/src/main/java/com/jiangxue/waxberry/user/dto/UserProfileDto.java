package com.jiangxue.waxberry.user.dto;

import com.jiangxue.waxberry.user.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String id;
    private String userId;
    private String uscc;
    private String companyName;
    private String companyAdmin;
    private String school;
    private String college;
    private String major;
    private String workNum;
    private String bio;
    private String industry;
    private String avatarUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String gender;
    private String title;
}
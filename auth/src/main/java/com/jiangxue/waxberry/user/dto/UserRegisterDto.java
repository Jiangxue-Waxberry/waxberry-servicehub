package com.jiangxue.waxberry.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String id;
    @NotBlank(message = "用户名不能为空")
    private String loginname;
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String userRole;
    private String email;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    private Boolean enabled;
    private String status;
    private int createNum;
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
    private String gender;
    private String title;
    private String code;
}
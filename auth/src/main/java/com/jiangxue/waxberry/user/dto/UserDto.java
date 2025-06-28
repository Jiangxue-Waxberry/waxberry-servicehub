package com.jiangxue.waxberry.user.dto;


import com.jiangxue.waxberry.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto{
    private String id;
    private String loginname;
    private String username;
    private String password;
    private String userRole;
    private String email;
    private String mobile;
    private Boolean enabled;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int createNum;
}
package com.jiangxue.waxberry.user.dto;

import com.jiangxue.waxberry.user.entity.User;
import lombok.Data;

@Data
public class UserStatusDto {
    private User.UserStatus status;
}


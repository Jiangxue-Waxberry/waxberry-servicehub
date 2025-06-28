package com.jiangxue.waxberry.auth.dto;

import lombok.Data;

@Data
public class MessageDto {

    private String userName;
    private String password;
    private String mobile;
    private String email;
    private String code;
    private Long seconds;
    private String message;
}

package com.jiangxue.waxberry.auth.controller;

import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.auth.dto.MessageDto;
import com.jiangxue.waxberry.auth.enums.LoginTypeEnum;
import com.jiangxue.waxberry.auth.service.MessageAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/sms")
public class SmsAuthController {

    @Autowired
    private MessageAuthService messageAuthService;

    @PostMapping("/{actionType}")
    public ApiResult messageAuth(@PathVariable("actionType") String actionType, @RequestBody MessageDto messageDto) throws Exception {
        switch (LoginTypeEnum.getLoginTypeEnum(actionType)) {
            case REGISTER:
                messageAuthService.handleRegister(messageDto);
                return ApiResult.success();
            case LOGIN:
                messageAuthService.handleLogin(messageDto);
                return ApiResult.success();
            case RESET_PASSWORD:
                messageAuthService.handleResetPassword(messageDto);
                return ApiResult.success();
        }
        return ApiResult.success();
    }
}

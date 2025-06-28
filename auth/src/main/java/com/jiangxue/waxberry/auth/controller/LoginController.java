package com.jiangxue.waxberry.auth.controller;

import com.jiangxue.framework.common.web.ApiResult;
import com.jiangxue.waxberry.auth.dto.MessageDto;
import com.jiangxue.waxberry.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/loginCheck")
    public ApiResult messageAuth(@RequestBody MessageDto messageDto) throws Exception {
        loginService.loginCheck(messageDto);
        return ApiResult.success();
    }
}

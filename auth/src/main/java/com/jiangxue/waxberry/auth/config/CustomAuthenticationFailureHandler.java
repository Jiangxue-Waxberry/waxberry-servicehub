package com.jiangxue.waxberry.auth.config;

import com.jiangxue.waxberry.auth.enums.ErrorCode;
import com.jiangxue.waxberry.auth.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import java.io.IOException;
import java.util.logging.Logger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger log = Logger.getLogger(CustomAuthenticationFailureHandler.class.getName());


    private final String loginPage;

    public CustomAuthenticationFailureHandler(String loginPage) {
        this.loginPage = loginPage;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 根据异常类型获取错误码
        ErrorCode errorCode = mapExceptionToErrorCode(exception);

        // 重定向到带错误参数的登录页面
        setDefaultFailureUrl(loginPage + "?error=" + errorCode);
        super.onAuthenticationFailure(request, response, exception);
    }

    private ErrorCode mapExceptionToErrorCode(AuthenticationException exception) {
        // 检查异常的根源是否是业务异常
        Throwable cause = exception.getCause();
        if (cause instanceof BusinessException) {
            return ((BusinessException) cause).getErrorCode();
        }

        // 根据异常类型映射错误码
        if (exception instanceof DisabledException) {
            return ErrorCode.USER_DISABLED;
        } else if (exception instanceof BadCredentialsException) {
            return ErrorCode.VERIFICATION_CODE_ERROR;
        } else if (exception instanceof UsernameNotFoundException) {
            return ErrorCode.USER_NOT_FOUND;
        } else if (exception instanceof LockedException) {
            return ErrorCode.USER_LOCKED;
        } else {
            return ErrorCode.SYSTEM_ERROR;
        }
    }
}

package com.jiangxue.waxberry.auth.config;

import com.jiangxue.waxberry.auth.dto.SmsAuthenticationToken;
import com.jiangxue.waxberry.auth.enums.ErrorCode;
import com.jiangxue.waxberry.auth.exception.BusinessException;
import com.jiangxue.waxberry.auth.service.RedisService;
import com.jiangxue.waxberry.user.entity.UserProcess;
import com.jiangxue.waxberry.user.repository.UserProcessRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    private RedisService redisService;

    private UserProcessRepository userProcessRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authToken = (SmsAuthenticationToken) authentication;
        String mobile = (String) authToken.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Optional<UserProcess> optionalUserProcess = userProcessRepository.findByMobile(mobile);
        if (optionalUserProcess.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        SmsAuthenticationToken authenticatedToken = new SmsAuthenticationToken(
                userDetails,
                null, // 清除验证码
                userDetails.getAuthorities()
        );
        authenticatedToken.setDetails(authToken.getDetails());

        redisService.delete(mobile);
        return authenticatedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setRedisServicet(RedisService redisService) {
        this.redisService = redisService;
    }

    public UserProcessRepository getUserProcessRepository() {
        return userProcessRepository;
    }

    public void setUserProcessRepository(UserProcessRepository userProcessRepository) {
        this.userProcessRepository = userProcessRepository;
    }
}


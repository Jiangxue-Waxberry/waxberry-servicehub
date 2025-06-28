package com.jiangxue.waxberry.auth.service;

import com.jiangxue.framework.common.enums.TemplateEnum;
import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.framework.common.util.SmsUtils;
import com.jiangxue.waxberry.auth.dto.MessageDto;
import com.jiangxue.waxberry.auth.enums.ErrorCode;
import com.jiangxue.waxberry.user.entity.User;
import com.jiangxue.waxberry.user.entity.UserProcess;
import com.jiangxue.waxberry.user.repository.UserProcessRepository;
import com.jiangxue.waxberry.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MessageAuthService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProcessRepository userProcessRepository;

    @Value("${auth.ALIBABA_CLOUD_ACCESS_KEY_SECRET}")
    private String accessKeySecret;

    @Value("${auth.ALIBABA_CLOUD_ACCESS_KEY_ID}")
    private String accessKeyId;

    private static final String ServerName = "auth:";




    public void handleRegister(MessageDto messageDto) throws Exception {
        // 注册逻辑：验证验证码、检查用户是否存在、创建新用户
        sendSmsResponse(messageDto,TemplateEnum.Register.getValue(),"register:");
    }

    public void handleLogin(MessageDto messageDto) throws Exception {
        User user;
        try {
            if (EMAIL_PATTERN.matcher(messageDto.getMobile()).matches()) {
                user = userRepository.findByEmail(messageDto.getMobile()).orElseThrow(() -> new UsernameNotFoundException("User not found with email or mobile"));
            } else {
                user = userRepository.findByMobile(messageDto.getMobile()).orElseThrow(() -> new UsernameNotFoundException("User not found with email or mobile"));
            }
        } catch (Exception e) {
            throw new BizException(ErrorCode.USER_NOT_REGISTER.getMessage());
        }
        String mobile = user.getMobile();
        checkUserProcess(mobile);
        // 登录逻辑：验证验证码、生成Token
        sendSmsResponse(messageDto,TemplateEnum.Login.getValue(), "login:");
    }

    public void handleResetPassword(MessageDto messageDto) throws Exception {
        // 修改密码逻辑：验证验证码、更新密码
        sendSmsResponse(messageDto,TemplateEnum.ResetPassword.getValue(), "resetPassword:");
    }

    public void sendSmsResponse(MessageDto messageDto, String templateType, String key) throws Exception {
        if (redisService.exists(ServerName + key + messageDto.getMobile())) {
            redisService.getExpire(messageDto.getMobile(), TimeUnit.MINUTES);
            throw new BizException("获取验证码频繁，请稍后重试");
        } else {
            SmsUtils.init(accessKeyId,accessKeySecret);
            String captcha = SmsUtils.sendSmsCode(messageDto.getMobile(),templateType);
            redisService.set(ServerName + key + messageDto.getMobile(),captcha,5,TimeUnit.MINUTES);
        }
    }

    public void checkUserProcess(String mobile) {
        Optional<UserProcess> optionalUserProcess = userProcessRepository.findByMobile(mobile);
        if (optionalUserProcess.isEmpty()) {
            throw new BizException(ErrorCode.USER_NOT_REGISTER.getMessage());
        }
        UserProcess userProcess = optionalUserProcess.get();
        if (UserProcess.ProcessStatus.PROCESS.equals(userProcess.getStatus())) {
            throw new BizException(ErrorCode.PENDING_APPROVAL.getMessage());
        } else if (UserProcess.ProcessStatus.REFUSE.equals(userProcess.getStatus())) {
            throw new BizException(ErrorCode.APPROVAL_REJECTED.getMessage());
        }
    }
}

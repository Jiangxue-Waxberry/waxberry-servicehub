package com.jiangxue.waxberry.auth.service;

import com.jiangxue.framework.common.exception.BizException;
import com.jiangxue.waxberry.auth.dto.MessageDto;
import com.jiangxue.waxberry.auth.enums.ErrorCode;
import com.jiangxue.waxberry.auth.utils.AppConfigUtil;
import com.jiangxue.waxberry.auth.utils.RSAUtil;
import com.jiangxue.waxberry.user.entity.User;
import com.jiangxue.waxberry.user.entity.UserProcess;
import com.jiangxue.waxberry.user.repository.UserProcessRepository;
import com.jiangxue.waxberry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.PrivateKey;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class LoginService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

//    @Value("${auth.privateKey}")
//    private  String privateKeyPem;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProcessRepository userProcessRepository;

    @Autowired
    private RedisService redisService;

    public void loginCheck(MessageDto messageDto) {
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
        checkLoginNum(user);
        String mobile = user.getMobile();
        checkUserProcess(mobile);
        if (!ObjectUtils.isEmpty(messageDto.getCode())) {
            String code = messageDto.getCode();
            String storedCode = redisService.get("auth:login:" + mobile);
            if (ObjectUtils.isEmpty(storedCode)) {
                throw new BizException(ErrorCode.VERIFICATION_CODE_EXPIRED.getMessage());
            }
            if (!storedCode.equals(code)) {
                throw new BizException(ErrorCode.VERIFICATION_CODE_ERROR.getMessage());
            }
        }
        if (!ObjectUtils.isEmpty(messageDto.getPassword())) {
            String decryptPassword = decrypt(messageDto.getPassword());
            checkPassword(decryptPassword,user.getPassword());
        }
        redisService.delete("auth:login:" + mobile);
    }

    public String decrypt(String password) {
        try {
            PrivateKey privateKey = RSAUtil.getPrivateKey(AppConfigUtil.getPrivateKeyPem());
            return RSAUtil.decrypt(password, privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkPassword(String decryptPassword, String  password) {
        if (!passwordEncoder.matches(decryptPassword,password)) {
            throw new BizException(ErrorCode.VERIFICATION_PASSWORD_ERROR.getMessage());
        }
    }
    public void checkLoginNum(User user) {
        String id = user.getId();
        String key = "auth:login:" + id;
        boolean exists = redisService.exists(key);
        if (exists) {
            Double clickScore = redisService.getClickScore(key, id);
            if (!ObjectUtils.isEmpty(clickScore) && clickScore.compareTo(5.0) > 0) {
                redisService.incrementClicks(key,id,1,TimeUnit.HOURS);
                Long expire = redisService.getExpire(key, TimeUnit.MINUTES);
                throw new BizException("该账号已锁定请于" + expire + "分钟后重试");
            }
        } else {
            redisService.incrementClicks(key,id,5,TimeUnit.MINUTES);
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

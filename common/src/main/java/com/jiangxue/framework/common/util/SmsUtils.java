package com.jiangxue.framework.common.util;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.jiangxue.framework.common.enums.TemplateEnum;
import com.jiangxue.framework.common.exception.BizException;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云短信服务工具类（静态方法版）
 */
public class SmsUtils {

    // 配置参数
    private static String ACCESS_KEY_ID;
    private static String ACCESS_KEY_SECRET;
    private static final String ENDPOINT = "dysmsapi.aliyuncs.com";
    private static final int MAX_RETRIES = 3;

    private static Client SMS_CLIENT;

    /**
     * 初始化配置（需在使用前调用）
     */
    public static void init(String accessKeyId, String accessKeySecret) {
        ACCESS_KEY_ID = Objects.requireNonNull(accessKeyId, "accessKeyId不能为null");
        ACCESS_KEY_SECRET = Objects.requireNonNull(accessKeySecret, "accessKeySecret不能为null");
    }

    /**
     * 获取短信客户端（单例模式）
     */
    private static synchronized Client getSmsClient() {
        if (SMS_CLIENT == null) {
            if (ACCESS_KEY_ID == null || ACCESS_KEY_SECRET == null) {
                throw new IllegalStateException("短信服务未初始化，请先调用SmsUtils.init()方法");
            }

            try {
                Config config = new Config()
                        .setAccessKeyId(ACCESS_KEY_ID)
                        .setAccessKeySecret(ACCESS_KEY_SECRET)
                        .setEndpoint(ENDPOINT);
                SMS_CLIENT = new Client(config);
            } catch (Exception e) {
                throw new BizException("短信客户端初始化失败", e);
            }
        }
        return SMS_CLIENT;
    }

    /**
     * 发送模板短信
     * @param mobile 手机号码
     * @param templateType 模板类型
     * @param templateParam 模板参数
     * @return 发送响应
     */
    public static SendSmsResponse sendTemplateMessage(String mobile, String templateType, Map<String, String> templateParam) {
        return sendSms(mobile, templateType, templateParam);
    }

    /**
     * 发送验证码短信
     * @param mobile 手机号码
     * @param templateType 模板类型
     * @return 生成的验证码
     */
    public static String sendSmsCode(String mobile, String templateType) {
        String verificationCode = generateRandomCode();
        Map<String, String> params = new HashMap<>();
        params.put("code", verificationCode);
        sendSms(mobile, templateType, params);
        return verificationCode;
    }

    /**
     * 核心短信发送方法
     */
    private static SendSmsResponse sendSms(String mobile, String templateCode, Map<String, String> templateParam) {
        try {
            SendSmsRequest request = buildSmsRequest(mobile, templateCode, templateParam);
            SendSmsResponse response = getSmsClient().sendSms(request);

            if (!"OK".equals(response.getBody().getCode())) {
                return retrySendSms(request);
            }
            return response;
        } catch (Exception e) {
            throw new BizException("短信发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 短信发送重试机制
     */
    private static SendSmsResponse retrySendSms(SendSmsRequest request) throws Exception {
        for (int i = 0; i < MAX_RETRIES; i++) {
            Thread.sleep((i + 1) * 1000L);
            SendSmsResponse response = getSmsClient().sendSms(request);
            if ("OK".equals(response.getBody().getCode())) {
                return response;
            }
        }
        throw new BizException("重试次数已达上限");
    }

    /**
     * 构建短信请求对象
     */
    private static SendSmsRequest buildSmsRequest(String mobile, String templateCode, Map<String, String> templateParam) {
        return new SendSmsRequest()
                .setPhoneNumbers(mobile)
                .setSignName(TemplateEnum.JIANGXUE.getValue())
                .setTemplateCode(templateCode)
                .setTemplateParam(JSONObject.toJSONString(templateParam));
    }

    /**
     * 生成6位随机验证码
     */
    private static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(900000) + 100000);
    }
}
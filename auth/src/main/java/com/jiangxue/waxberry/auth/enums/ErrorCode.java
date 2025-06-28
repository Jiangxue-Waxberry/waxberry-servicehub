package com.jiangxue.waxberry.auth.enums;

public enum ErrorCode {
    USER_NOT_REGISTER("user_not_register", "用户未注册"),
    USER_LOCKED("user_locked", "账号已锁定"),
    USER_DISABLED("user_disabled", "用户已禁用"),
    VERIFICATION_CODE_EXPIRED("verification_code_expired", "验证码已过期，请重新获取"),
    VERIFICATION_CODE_ERROR("code_bad_credentials", "验证码错误"),
    VERIFICATION_PASSWORD_ERROR("password_bad_credentials", "密码错误"),
    USER_NOT_FOUND("user_not_found", "用户不存在"),
    DISABLED("disabled", "用户已禁用"),
    LOCKED("locked", "账号已锁定"),
    PENDING_APPROVAL("user_not_approval", "账号正在审核中，请耐心等待"),
    APPROVAL_REJECTED("approval_rejected", "账号未通过审核，请修改信息后重新申请"),
    SYSTEM_ERROR("system_error", "系统内部错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

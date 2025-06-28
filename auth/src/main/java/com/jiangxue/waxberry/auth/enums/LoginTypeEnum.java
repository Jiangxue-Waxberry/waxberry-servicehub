package com.jiangxue.waxberry.auth.enums;

public enum LoginTypeEnum {
    REGISTER("smsRegister"),
    LOGIN("smsLogin"),
    RESET_PASSWORD("smsResetPassword");
    private final String value;

    LoginTypeEnum(String value) {
        this.value = value;
    }

    public static LoginTypeEnum getLoginTypeEnum(String value) {
        for (LoginTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid camelCase action type: " + value);
    }
}

package com.jiangxue.framework.common.web;

@Deprecated
public enum ResponseCode {
    SUCCESS(200, "成功"),
    ERROR(400, "失败");

    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

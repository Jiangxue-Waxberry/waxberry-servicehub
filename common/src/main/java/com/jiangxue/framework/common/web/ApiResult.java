package com.jiangxue.framework.common.web;

import lombok.Data;

@Data
public class ApiResult<T> {

    private int code;
    private String message;
    private T data;
    private boolean success;

    public ApiResult() {}

    private ApiResult(Builder<T> builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
        this.success = builder.success;

    }

    public static <T> ApiResult<T> success(T data) {
        return new Builder<T>().withCode(ResponseCode.SUCCESS.getCode())
                .withMessage(ResponseCode.SUCCESS.getMessage())
                .withSuccess(true)
                .withData(data)
                .build();
    }

    public static ApiResult<Void> success() {
        return new Builder<Void>().withCode(ResponseCode.SUCCESS.getCode())
                .withSuccess(true)
                .withMessage(ResponseCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> ApiResult<T> error(String message) {
        return new Builder<T>().withCode(ResponseCode.ERROR.getCode())
                .withMessage(message)
                .withSuccess(false)
                .build();
    }

    // Standard getters and setters...

    public static class Builder<T> {
        private int code;
        private String message;
        private T data;
        private boolean success;

        public Builder<T> withCode(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> withData(T data) {
            this.data = data;
            return this;
        }

        public ApiResult<T> build() {
            return new ApiResult<>(this);
        }
    }
}

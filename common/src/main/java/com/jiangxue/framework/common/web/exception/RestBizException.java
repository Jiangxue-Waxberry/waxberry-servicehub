package com.jiangxue.framework.common.web.exception;

public class RestBizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code = 422;

    public RestBizException(final String message) {
        super(message);
    }

    public RestBizException(final Integer code, final String message) {
        super(message);
        this.code = code;
    }

    public RestBizException(final Integer code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }
}


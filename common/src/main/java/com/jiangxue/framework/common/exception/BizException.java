package com.jiangxue.framework.common.exception;

public class BizException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public BizException(String message) {
        super(message);
    }

    public BizException(Exception e) {
        super(e);
    }

    public BizException(String s, Exception e) {
        super(s,e);
    }
}

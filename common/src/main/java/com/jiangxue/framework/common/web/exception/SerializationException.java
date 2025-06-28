package com.jiangxue.framework.common.web.exception;

@SuppressWarnings("serial")
public class SerializationException extends RuntimeException {

    public SerializationException() {
        super();
    }

    public SerializationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SerializationException(final String message) {
        super(message);
    }

    public SerializationException(final Throwable cause) {
        super(cause);
    }
}

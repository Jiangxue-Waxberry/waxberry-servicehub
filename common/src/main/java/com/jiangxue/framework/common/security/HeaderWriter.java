package com.jiangxue.framework.common.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Contract for writing headers to a {@link HttpServletResponse}
 */
public interface HeaderWriter {

    /**
     * Create a {@code Header} instance.
     * @param request the request
     * @param response the response
     */
    void writeHeaders(HttpServletRequest request, HttpServletResponse response);

}

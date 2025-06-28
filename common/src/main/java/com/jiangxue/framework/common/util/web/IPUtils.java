package com.jiangxue.framework.common.util.web;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

abstract public class IPUtils {

    protected static final Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取服务端ip
     */
    public static String getServerIP(){
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("can not get server ip, unknown host, return empty.");
        }
        return ip;
    }


    /**
     * 获取访问的客户端ip
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request){
        if(request == null){
            logger.error("can not get client ip, request is null, return empty.");
            return "";
        }
        String ip = request.getParameter("ip");
        if (isIPEmpty(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (isIPEmpty(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isIPEmpty(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isIPEmpty(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isIPEmpty(String ip) {
        return ip==null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }


}

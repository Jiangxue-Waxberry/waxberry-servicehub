package com.jiangxue.framework.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Named;


/**
 * ExceptionResolver responsible for logging exception
 */
@Named("loggingHandlerExceptionResolver")
public class LoggingHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
    
    protected Logger logger = LoggerFactory.getLogger(LoggingHandlerExceptionResolver.class);
    
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }



    @Override
    public ModelAndView resolveException(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler, Exception ex) {
        logger.warn("Exception catched by Spring MVC: ", ex);
        return null; // trigger other HandlerExceptionResolver's
    }
}

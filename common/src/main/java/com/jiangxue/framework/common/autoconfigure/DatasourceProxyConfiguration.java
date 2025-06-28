//package com.lingyframework.common.autoconfigure;
//
//import com.lingyframework.common.db.DatasourceProxyBeanPostProcessor;
//import net.ttddyy.dsproxy.listener.logging.CommonsLogLevel;
//import net.ttddyy.dsproxy.support.CommonsQueryCountLoggingServletFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//
//import java.util.Arrays;
//
//public class DatasourceProxyConfiguration {
//    @Bean
//    public DatasourceProxyBeanPostProcessor datasourceProxyBeanPostProcessor() {
//        return new DatasourceProxyBeanPostProcessor();
//    }
//
//    @Bean
//    public FilterRegistrationBean registryBaseFilter() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        CommonsQueryCountLoggingServletFilter commonsQueryCountLoggingServletFilter = new CommonsQueryCountLoggingServletFilter();
//        commonsQueryCountLoggingServletFilter.setLogLevel(CommonsLogLevel.DEBUG);
//        filterRegistrationBean.setFilter(commonsQueryCountLoggingServletFilter);
//        filterRegistrationBean.setOrder(-1);
//        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
//        filterRegistrationBean.setName("queryCountFilter");
//        return filterRegistrationBean;
//    }
//}

package com.jiangxue.framework.common.web;//package com.sysware.cloud.scene.common.web;
//
//import co.elastic.apm.api.ElasticApm;
//import co.elastic.apm.api.Transaction;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@ControllerAdvice
//public class TraceInfoAdvice implements ResponseBodyAdvice<Object> {
//
//    @Override
//    public boolean supports(final MethodParameter returnType, final Class<? extends HttpMessageConverter<?>> converterType) {
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        Transaction transaction = ElasticApm.currentTransaction();
//        String traceId = transaction.getTraceId();
//        if (StringUtils.isNotEmpty(traceId)) {
//            response.getHeaders().add("S-Trace-Id", traceId);
//        }
//
//        return body;
//    }
//}

//package com.lingyframework.common.db;
//
//import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
//import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
//import org.aopalliance.intercept.MethodInterceptor;
//import org.aopalliance.intercept.MethodInvocation;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.core.env.Environment;
//import org.springframework.util.ReflectionUtils;
//
//import javax.sql.DataSource;
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//
//public class DatasourceProxyBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {
//
//    private Environment environment;
//
//    @Override
//    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
//        if (bean instanceof DataSource) {
//            ProxyFactory factory = new ProxyFactory(bean);
//            factory.setProxyTargetClass(true);
//            Long threshold = environment.getProperty("db.slow-query-threshold", Long.class, 1000L);
//            factory.addAdvice(new ProxyDataSourceInterceptor((DataSource) bean, threshold));
//            return factory.getProxy();
//        }
//        return bean;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }
//
//    private static class ProxyDataSourceInterceptor implements MethodInterceptor {
//        private final DataSource dataSource;
//
//        public ProxyDataSourceInterceptor(final DataSource dataSource, final Long thresholdTime) {
//            super();
//            this.dataSource = ProxyDataSourceBuilder.create(dataSource).countQuery()
//                    .logSlowQueryBySlf4j(thresholdTime, TimeUnit.MILLISECONDS, SLF4JLogLevel.WARN)
//                    .logQueryBySlf4j(SLF4JLogLevel.DEBUG).build();
//        }
//
//        @Override
//        public Object invoke(final MethodInvocation invocation) throws Throwable {
//            Method proxyMethod = ReflectionUtils.findMethod(dataSource.getClass(), invocation.getMethod().getName());
//            if (proxyMethod != null) {
//                return proxyMethod.invoke(dataSource, invocation.getArguments());
//            }
//            return invocation.proceed();
//        }
//    }
//}

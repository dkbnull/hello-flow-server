package cn.wbnull.helloflow.app.config;

import cn.wbnull.helloflow.app.filter.TraceIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Web过滤器配置，注册TraceId过滤器并确保其在Spring Security之前执行
 *
 * @author null
 * @date 2026-06-01
 */
@Configuration
public class WebFilterConfig {

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration(TraceIdFilter traceIdFilter) {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(traceIdFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("traceIdFilter");
        return registration;
    }
}

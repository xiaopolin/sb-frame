package com.xpl.framework.config;

import com.xpl.framework.security.filter.SimpleFilter;
import com.xpl.framework.security.interceptor.SimpleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private SimpleInterceptor simpleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(simpleInterceptor).addPathPatterns("/**").excludePathPatterns("/user/exclude");
    }

    @Bean
    public FilterRegistrationBean registerFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new SimpleFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("simpleFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}

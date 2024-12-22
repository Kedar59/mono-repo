package com.apiGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> registrationBean =
                new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}

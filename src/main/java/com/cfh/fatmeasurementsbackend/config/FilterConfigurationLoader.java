package com.cfh.fatmeasurementsbackend.config;

import com.cfh.fatmeasurementsbackend.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Configuration
public class FilterConfigurationLoader {

    @Bean
    public AuthFilter authFilterRegistration() {
        return new AuthFilter();
    }

    /**
     * 注册AuthFilter过滤器
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean edsAuthenticationFilterRegistrationBean(AuthFilter filter) {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setName("authFilter");
        filterRegistrationBean.setOrder(5);

        List<String> urlPatterns=new ArrayList<String>();
        urlPatterns.add("/a/*");
        urlPatterns.add("/");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }
}
package com.sparrow.chat.boot.config;

import javax.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcChatConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Bean
    LoginUserFilter loginTokenFilter() {
        return new LoginUserFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> loginTokenFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginTokenFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("loginTokenFilter");
        filterRegistrationBean.setOrder(0);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }
}

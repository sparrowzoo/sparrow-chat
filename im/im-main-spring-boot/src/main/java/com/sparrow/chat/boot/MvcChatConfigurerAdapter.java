package com.sparrow.chat.boot;

import com.sparrow.mq.DefaultQueueHandlerMappingContainer;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.support.Authenticator;
import com.sparrow.support.DefaultAuthenticatorService;
import com.sparrow.support.web.MonolithicLoginUserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class MvcChatConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);


    @Autowired
    private SparrowConfig sparrowConfig;

    @Bean
    Authenticator authenticator() {
        SparrowConfig.Authenticator authenticatorConfig=this.sparrowConfig.getAuthenticator();
        return new DefaultAuthenticatorService(authenticatorConfig.getEncryptKey(),
                authenticatorConfig.getValidateDeviceId(),
                authenticatorConfig.getValidateStatus());
    }

    @Bean
    MonolithicLoginUserFilter loginTokenFilter() {
        SparrowConfig.Authenticator authenticatorConfig=this.sparrowConfig.getAuthenticator();
        SparrowConfig.Exception exceptionConfig=this.sparrowConfig.getException();
        return new MonolithicLoginUserFilter(authenticator(),
                authenticatorConfig.getMockLoginUser(),
                null,
                exceptionConfig.getSupportTemplate(),
                exceptionConfig.getApiPrefix());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Bean
    public FilterRegistrationBean<Filter> loginTokenFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginTokenFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("loginTokenFilter");
        filterRegistrationBean.addInitParameter("excludePatterns",
                this.sparrowConfig.getAuthenticator().getExcludePatterns());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public EventHandlerMappingContainer eventHandlerMappingContainer() {
        return new DefaultQueueHandlerMappingContainer();
    }
}

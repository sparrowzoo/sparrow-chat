package com.sparrow.chat.boot.config;

import com.sparrow.file.servlet.FileDownLoad;
import com.sparrow.file.servlet.FileUpload;
import com.sparrow.mq.DefaultQueueHandlerMappingContainer;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.filter.AccessMonitorFilter;
import com.sparrow.spring.starter.monitor.Monitor;
import com.sparrow.support.Authenticator;
import com.sparrow.support.DefaultAuthenticatorService;
import com.sparrow.support.IpSupport;
import com.sparrow.support.web.MonolithicLoginUserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import javax.servlet.Filter;

@Configuration
public class ContactMvcConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Inject
    private IpSupport ipSupport;
    @Autowired
    private SparrowConfig sparrowConfig;

    @Bean
    public Monitor monitor() {
        return new Monitor(this.ipSupport);
    }

    @Bean
    public AccessMonitorFilter accessMonitorFilter() {
        return new AccessMonitorFilter(monitor(), -99);
    }


    @Bean
    public ServletRegistrationBean fileUpload() {
        return new ServletRegistrationBean(new FileUpload(), "/file-upload");
    }

    @Bean
    public ServletRegistrationBean fileDownload() {
        return new ServletRegistrationBean(new FileDownLoad(), "/file-download");
    }

    @Bean
    Authenticator authenticator() {
        SparrowConfig.Authenticator authenticatorConfig = this.sparrowConfig.getAuthenticator();
        return new DefaultAuthenticatorService(authenticatorConfig.getEncryptKey(),
                authenticatorConfig.getValidateDeviceId(),
                authenticatorConfig.getValidateStatus());
    }

    @Bean
    MonolithicLoginUserFilter loginTokenFilter() {
        SparrowConfig.Authenticator authenticatorConfig = this.sparrowConfig.getAuthenticator();
        SparrowConfig.Mvc mvc = this.sparrowConfig.getMvc();
        return new MonolithicLoginUserFilter(
                authenticator(),
                authenticatorConfig.getMockLoginUser(),
                authenticatorConfig.getTokenKey(),
                mvc.getSupportTemplateEngine(),
                authenticatorConfig.getExcludePatterns(),
                mvc.getAjaxPattens()
        );
    }

    @Bean
    public FilterRegistrationBean<Filter> loginTokenFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginTokenFilter());
        // 一个* 不允许多个***
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("loginTokenFilter");
        filterRegistrationBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public EventHandlerMappingContainer eventHandlerMappingContainer() {
        return new DefaultQueueHandlerMappingContainer();
    }
}

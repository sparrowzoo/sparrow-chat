package com.sparrow.chat.boot;

import com.sparrow.mq.DefaultQueueHandlerMappingContainer;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.support.Authenticator;
import com.sparrow.support.DefaultAuthenticatorService;
import com.sparrow.support.web.MonolithicLoginUserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class MvcChatConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Value("${mock_login_user}")
    private Boolean mockLoginUser;

    @Value("${authenticator.encrypt_key}")
    private String encryptKey;

    @Value("${authenticator.validate_device_id}")
    private Boolean validateDeviceId;

    @Value("${authenticator.validate_status}")
    private Boolean validateStatus;

    @Value("${authenticator.white.list}")
    private List<String> whiteList;

    private static final String EXCLUSIONS = "/doc.html,/webjars/**";

    @Bean
    Authenticator authenticator() {
        return new DefaultAuthenticatorService(this.encryptKey, this.validateDeviceId, this.validateStatus);
    }

    @Bean
    MonolithicLoginUserFilter loginTokenFilter() {
        return new MonolithicLoginUserFilter(authenticator(), this.mockLoginUser, this.whiteList, null);
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
        filterRegistrationBean.addUrlPatterns("/");
        filterRegistrationBean.setName("loginTokenFilter");
        filterRegistrationBean.addInitParameter("excludePatterns", EXCLUSIONS);
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    @Bean
    public EventHandlerMappingContainer eventHandlerMappingContainer() {
        return new DefaultQueueHandlerMappingContainer();
    }
}

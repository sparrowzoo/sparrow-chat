package com.sparrow.chat.boot;

import com.sparrow.support.Authenticator;
import com.sparrow.support.web.MonolithicLoginUserFilter;
import javax.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcChatConfigurerAdapter implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Value("${mock_login_user}")
    private Boolean mockUser;

    @Bean
    Authenticator authenticator() {
        return new AuthenticatorService();
    }

    @Bean
    MonolithicLoginUserFilter loginTokenFilter() {
        return new MonolithicLoginUserFilter(authenticator(), this.mockUser);
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
        filterRegistrationBean.setOrder(2);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }
}

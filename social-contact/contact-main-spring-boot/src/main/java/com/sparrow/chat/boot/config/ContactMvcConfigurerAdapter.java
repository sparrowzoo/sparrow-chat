package com.sparrow.chat.boot.config;

import com.sparrow.chat.boot.ValidateCode;
import com.sparrow.passport.authenticate.AuthenticatorService;
import com.sparrow.spring.starter.resolver.ClientInfoArgumentResolvers;
import com.sparrow.spring.starter.resolver.LoginUserArgumentResolvers;
import com.sparrow.support.Authenticator;
import com.sparrow.support.web.GlobalAttributeFilter;
import com.sparrow.support.web.MonolithicLoginUserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import java.util.List;

@Configuration
public class ContactMvcConfigurerAdapter extends WebMvcConfigurationSupport {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Value("${mock_login_user}")
    private Boolean mockUser;


    @Inject
    private ClientInfoArgumentResolvers clientInfoArgumentResolvers;

    @Inject
    private LoginUserArgumentResolvers loginTokenArgumentResolvers;


    @Bean
    public ServletRegistrationBean validateCode() {
        return new ServletRegistrationBean(new ValidateCode(), "/validate-code");
    }

    @Bean
    Authenticator authenticator() {
        return new AuthenticatorService();
    }

    @Bean
    MonolithicLoginUserFilter loginTokenFilter() {
        return new MonolithicLoginUserFilter(authenticator(), this.mockUser);
    }

    @Bean
    public GlobalAttributeFilter globalAttributeFilter() {
        return new GlobalAttributeFilter();
    }


    /**
     * 兼容swagger 配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.setOrder(-1);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.clientInfoArgumentResolvers);
        argumentResolvers.add(this.loginTokenArgumentResolvers);
    }

    @Bean
    @ConditionalOnMissingBean({RequestContextListener.class, RequestContextFilter.class})
    @ConditionalOnMissingFilterBean(RequestContextFilter.class)
    public static RequestContextFilter requestContextFilter() {
        return new OrderedRequestContextFilter();
    }
}

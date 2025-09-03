package com.sparrow.chat.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@ComponentScan("com.sparrow.chat")
@EnableSwagger2WebMvc
public class ChatAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(ApiInfo.class)
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Sparrow Community").description("Sparrow Community").termsOfServiceUrl("www.sparrowzoo.com").contact(new Contact("harry", "http://www.sparrowzoo.com", "zh_harry@163.com")).version("1.0").build();
    }

    @Bean
    public Docket chatDocket(ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).groupName("IM").select().apis(
                RequestHandlerSelectors.basePackage("com.sparrow.chat.contact.controller")
                        .or(RequestHandlerSelectors.basePackage("com.sparrow.chat.im.controller"))
                        .or(RequestHandlerSelectors.basePackage("com.sparrow.chat.boot.controller"))

        ).paths(PathSelectors.any()).build();
    }
}

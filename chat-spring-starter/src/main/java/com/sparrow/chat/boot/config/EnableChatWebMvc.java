package com.sparrow.chat.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
//@Import({PassportWebMvcConfiguration.class})
@ComponentScan(
        basePackages = {"com.sparrow.chat"}
)
public @interface EnableChatWebMvc {
}

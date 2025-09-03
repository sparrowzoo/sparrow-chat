package com.sparrow.chat.boot.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ChatAutoConfiguration.class})
public @interface EnableChatApp {
}

package com.sparrow.chat.boot.config;

import com.sparrow.spring.starter.advice.ControllerResponseAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.sparrow.chat"})
public class ContactControllerAdvice extends ControllerResponseAdvice {
}

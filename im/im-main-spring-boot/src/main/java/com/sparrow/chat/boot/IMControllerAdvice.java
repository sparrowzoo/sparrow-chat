package com.sparrow.chat.boot;

import com.sparrow.spring.starter.advice.ControllerResponseAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.sparrow.chat.adapter.controller"})
public class IMControllerAdvice extends ControllerResponseAdvice {
}

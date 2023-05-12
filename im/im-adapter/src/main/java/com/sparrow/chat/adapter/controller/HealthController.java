package com.sparrow.chat.adapter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthController {
    @RequestMapping("health")
    public String health() {
        return "ok";
    }
}

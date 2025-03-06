package com.sparrow.chat.adapter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "健康检查", tags = "健康检查")

@RestController
@RequestMapping("/")
public class HealthController {
    @GetMapping("health")
    @ApiOperation(value = "健康检查", tags = "健康检查")
    public String health() {
        return "ok";
    }
}

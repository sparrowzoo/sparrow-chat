package com.sparrow.chat.im.controller;

import com.sparrow.spring.starter.config.SparrowConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "健康检查", tags = "健康检查")

@RestController
@RequestMapping("/")
public class HealthController {

    @Autowired
    private SparrowConfig sparrowConfig;

    @GetMapping("health")
    @ApiOperation(value = "健康检查", tags = "健康检查")
    public String health() {
        return "ok";
    }


    @GetMapping("config")
    public String getSparrowConfig() {
        return sparrowConfig.toString();
    }
}

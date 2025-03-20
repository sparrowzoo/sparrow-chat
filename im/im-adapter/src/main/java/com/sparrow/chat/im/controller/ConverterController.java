package com.sparrow.chat.im.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "基础类型转换", tags = "基础类型转换")
@RestController
@RequestMapping("/message/converter")
public class ConverterController {
    @ApiOperation("布尔值转换")
    @GetMapping("boolean")
    public Boolean testBoolean() {
        return true;
    }

    @GetMapping("/int")
    @ApiOperation("integer转换")

    public Integer testInteger() {
        return 1;
    }

    @GetMapping("/long")
    @ApiOperation("long转换")

    public Integer testLong() {
        return 1;
    }

    @GetMapping("/float")
    @ApiOperation("float转换")

    public Float testFloat() {
        return 1F;
    }

    @GetMapping("/byte")
    @ApiOperation("byte转换")

    public Byte testByte() {
        return 1;
    }
}

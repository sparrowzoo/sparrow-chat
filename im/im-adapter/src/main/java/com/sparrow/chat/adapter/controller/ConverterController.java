package com.sparrow.chat.adapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/converter")
public class ConverterController {
    @GetMapping("boolean")
    public Boolean testBoolean() {
        return true;
    }

    @GetMapping("/int")
    public Integer testInteger() {
        return 1;
    }

    @GetMapping("/long")
    public Integer testLong() {
        return 1;
    }

    @GetMapping("/float")
    public Float testFloat() {
        return 1F;
    }

    @GetMapping("/byte")
    public Byte testByte() {
        return 1;
    }
}

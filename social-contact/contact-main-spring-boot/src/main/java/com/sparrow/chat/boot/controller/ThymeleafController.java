package com.sparrow.chat.boot.controller;

import com.sparrow.passport.po.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ThymeleafController {
    @RequestMapping("thymeleaf")
    public ModelAndView thymeleaf(HttpServletRequest request) {
        User user = new User();
        user.setUserId(1L);
        request.setAttribute("user", user);
        return new ModelAndView("/thymeleaf-test");
    }
}

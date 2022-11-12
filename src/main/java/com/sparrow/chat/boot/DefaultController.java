//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparrow.chat.boot;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {
    public DefaultController() {
    }

    @GetMapping({"/**"})
    public ModelAndView all(HttpServletRequest request) {
        return new ModelAndView(request.getServletPath());
    }
}

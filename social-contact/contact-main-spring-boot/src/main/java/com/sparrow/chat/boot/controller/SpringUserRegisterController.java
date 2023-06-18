package com.sparrow.chat.boot.controller;

import com.sparrow.cache.exception.CacheNotFoundException;
import com.sparrow.passport.controller.UserRegisterController;
import com.sparrow.passport.protocol.param.register.EmailActivateParam;
import com.sparrow.passport.protocol.param.register.EmailRegisterParam;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/register")
public class SpringUserRegisterController {

    @Autowired
    private UserRegisterController userRegisterController;

    @PostMapping("/email/shortcut")
    public Result<Boolean> shortcut(EmailRegisterParam user,
        ClientInformation client) throws BusinessException, CacheNotFoundException {
        userRegisterController.emailRegister(user, client);
        Result<Boolean> result = new Result<>(true);
        result.setMessage("激活邮件发送成功！！");
        return result;
    }

    @PostMapping("/email")
    public ModelAndView emailRegister(EmailRegisterParam user,
        ClientInformation client, RedirectAttributes attributes) throws BusinessException, CacheNotFoundException {
        this.userRegisterController.emailRegister(user, client);
        ModelAndView mv = new ModelAndView("redirect:/email-activate-send-success");
        mv.addObject("email", user.getEmail());
        attributes.addFlashAttribute("email2", user.getEmail());
        return mv;
    }

    @PostMapping("/email/activate/send.json")
    public Result<Boolean> sendActivateEmail(EmailActivateParam user,
        ClientInformation client) throws BusinessException {
        this.userRegisterController.sendTokenToEmail(user, client);
        Result<Boolean> result = new Result<>(true);
        result.setMessage("激活邮件发送成功！！");
        return result;
    }

    @GetMapping("/email/activate")
    public ModelAndView activeEmail(String token, ClientInformation client) throws BusinessException {
        this.userRegisterController.activateEmail(token, client);
        return new ModelAndView("redirect:/email-activate-success");
    }
}

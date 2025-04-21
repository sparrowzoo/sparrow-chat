package com.sparrow.chat.boot.controller;

import com.sparrow.passport.controller.UserRegisterController;
import com.sparrow.passport.protocol.param.register.EmailActivateParam;
import com.sparrow.passport.protocol.param.register.EmailRegisterParam;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/register")
public class SpringUserRegisterController {

    @Autowired
    private UserRegisterController userRegisterController;

    @PostMapping("/email/shortcut.json")
    public Result<Boolean> shortcut(@RequestBody EmailRegisterParam user,
                                    ClientInformation client) throws BusinessException {
        userRegisterController.emailRegister(user, client);
        return new Result<>(true, "激活邮件发送成功！！");
    }

    @PostMapping("/email")
    public ModelAndView emailRegister(EmailRegisterParam user,
                                      ClientInformation client, RedirectAttributes attributes) throws BusinessException {
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
        return new Result<>(true, "激活邮件发送成功！！");
    }

    @GetMapping("/email/activate")
    public ModelAndView activeEmail(String token, ClientInformation client) throws BusinessException {
        this.userRegisterController.activateEmail(token, client);
        return new ModelAndView("redirect:/email-activate-success");
    }
}

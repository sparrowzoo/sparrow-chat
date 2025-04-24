package com.sparrow.chat.boot.controller;

import com.sparrow.passport.controller.UserSecurityController;
import com.sparrow.passport.protocol.param.password.PasswordResetParam;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

@RestController
@RequestMapping("/password")
@Api(value = "password", tags = "密码")
public class SpringUserSecurityController {
    @Inject
    private UserSecurityController userSecurityController;

    @PostMapping("/send-find-password-email.json")
    public Boolean sendEmailTokenForFindPassword(@RequestBody String email) throws BusinessException {
        return this.userSecurityController.sendEmailTokenForFindPassword(email);
    }

    @GetMapping("/token-verify")
    public ModelAndView tokenVerify(String token) throws BusinessException {
        this.userSecurityController.tokenVerify(token);
        return new ModelAndView("/password/token-verify");
    }

    @PostMapping("/reset-password-by-token.json")
    public boolean resetPassword(@RequestBody PasswordResetParam param) throws BusinessException {
        this.userSecurityController.resetPassword(param);
        return true;
    }
}

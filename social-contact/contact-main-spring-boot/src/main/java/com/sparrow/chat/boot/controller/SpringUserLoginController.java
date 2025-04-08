package com.sparrow.chat.boot.controller;

import com.sparrow.passport.controller.UserLoginController;
import com.sparrow.passport.protocol.dto.LoginDTO;
import com.sparrow.passport.protocol.enums.PassportError;
import com.sparrow.passport.protocol.query.login.LoginQuery;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "login", tags = "登录")
public class SpringUserLoginController {
    @Autowired
    private UserLoginController userLoginController;


    @GetMapping("/session-id")
    public String sessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    //sparrow mvc 模式下 为了与login 区分开
    @PostMapping("/login.do")
    /**
     * @RequestBody DispatcherServlet Completed 415 UNSUPPORTED_MEDIA_TYPE
     */
    public ModelAndView login(LoginQuery login,
                              ClientInformation client) throws BusinessException {
        try {
            LoginDTO loginDto = this.userLoginController.login(login, client);
            ModelAndView mv = new ModelAndView(login.getRedirectUrl());
            mv.addObject(loginDto);
            return mv;
        } catch (BusinessException e) {
            if (e.getErrorSupport().getCode().equals(PassportError.USER_NOT_ACTIVATE.getCode())) {
                ModelAndView mv = new ModelAndView("redirect:/email-activate");
                mv.addObject("email", login.getUserName());
                return mv;
            }
            throw e;
        }
    }

    @PostMapping("/shortcut-login.json")
    public LoginDTO shortcut(@RequestBody LoginQuery login, ClientInformation client) throws BusinessException {
        return this.userLoginController.shortcut(login, client);
    }

    @ApiOperation(value = "获取访客Token")
    @GetMapping("/chat/v2/get-visitor-token.json")
    public String getVisitorToken(ClientInformation client) {
        return this.userLoginController.getVisitorToken(client).getToken();
    }

    public void logout() {
        this.userLoginController.logout();
    }
}

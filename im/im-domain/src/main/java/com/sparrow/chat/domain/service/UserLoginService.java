package com.sparrow.chat.domain.service;

import com.sparrow.protocol.LoginUser;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLoginService {

    @Autowired
    private ServletContainer springServletContainer;

    @Autowired
    private Authenticator authenticator;


    public String login(Long userId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId);
        loginUser.setUserName("张" + userId);
        loginUser.setCategory(LoginUser.CATEGORY_REGISTER);
        loginUser.setNickName(loginUser.getUserName());
        loginUser.setAvatar("");
        loginUser.setDays(1D);
        loginUser.setDeviceId(springServletContainer.getClientIp());
        loginUser.setExpireAt(System.currentTimeMillis() + 3600 * 1000 * 24);
        return this.authenticator.sign(loginUser, null);
    }
}

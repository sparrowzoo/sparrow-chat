package com.sparrow.chat.domain.service;

import com.sparrow.authenticator.AuthenticationInfo;
import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLoginService {

    @Autowired
    private ServletContainer springServletContainer;

    @Autowired
    private Authenticator authenticator;


    public String login(Long userId) throws BusinessException {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUserId(userId);
        loginUser.setUserName("张" + userId);
        loginUser.setCategory(LoginUser.CATEGORY_REGISTER);
        loginUser.setNickName(loginUser.getUserName());
        loginUser.setAvatar("");
        loginUser.setDays(1D);
        loginUser.setHost(springServletContainer.getClientIp());
        loginUser.setExpireAt(System.currentTimeMillis() + 3600 * 1000 * 24);
        return this.authenticator.login(new AuthenticationInfo() {
            @Override
            public LoginUser getUser() {
                return loginUser;
            }

            @Override
            public String getCredential() {
                return "";
            }
        });
    }
}

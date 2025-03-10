package com.sparrow.chat.domain.service;

import com.sparrow.chat.domain.repository.VisitorRepository;
import com.sparrow.protocol.LoginUser;
import com.sparrow.support.Authenticator;
import com.sparrow.support.IpSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisitorService {
    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private IpSupport ipSupport;

    @Autowired
    private Authenticator authenticator;

    public String generateVisitorToken() {
        Long visitorId = this.visitorRepository.getVisitorId();
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(visitorId);
        loginUser.setUserName(visitorId.toString());
        loginUser.setCategory(LoginUser.CATEGORY_VISITOR);
        loginUser.setUserName("访客:" + visitorId);
        loginUser.setNickName(loginUser.getUserName());
        loginUser.setAvatar("");
        loginUser.setDays(Integer.MAX_VALUE);
        loginUser.setDeviceId(ipSupport.getLocalIp());
        loginUser.setExpireAt(System.currentTimeMillis() + 3600 * 1000 * 24);
        return this.authenticator.sign(loginUser, null);
    }
}

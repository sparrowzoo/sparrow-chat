package com.sparrow.im;

import com.sparrow.chat.boot.Application;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import com.sparrow.support.Authenticator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SignTest {
    @Autowired
    private Authenticator authenticator;

    @Test
    public void testSign() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(0L);
        loginUser.setNickName("zhangsan");
        loginUser.setUserName("zhangsan");
        loginUser.setAvatar("ddd");
        loginUser.setDeviceId("127.0.0.1");
        loginUser.setDays(2);
        loginUser.setExpireAt(System.currentTimeMillis() + 2000000000L);
        loginUser.setExtensions(new HashMap<String, Object>());
        loginUser.setCategory(LoginUser.CATEGORY_REGISTER);

        LoginUserStatus status = new LoginUserStatus(1,loginUser.getExpireAt());

        String sign = this.authenticator.sign(loginUser, status);
        System.out.println(sign);
    }
}

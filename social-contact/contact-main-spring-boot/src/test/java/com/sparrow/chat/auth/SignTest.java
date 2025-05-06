package com.sparrow.chat.auth;

import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.protocol.BusinessException;
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
@SpringBootTest(classes = {ApplicationBoot.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SignTest {
    @Autowired
    private Authenticator authenticator;

    @Test
    public void testSign() throws BusinessException {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
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

//        LoginUser loginUser = new LoginUser();
//        loginUser.setUserId(46L);
//        loginUser.setTenantId("9def9391-9d24-4488-867e-594cfc35d254");
//        loginUser.setCategory(2);
//        String encryptKey = "7xVtRkGpLq9Yfz2wM8nJcB4hDmK3Td6AeXqP1oW0ySvF5U=";
//        String token = sign(loginUser,encryptKey );
//        System.out.println("token->"+token);



   String token="eyJjYXRlZ29yeSI6MiwiZXh0ZW5zaW9ucyI6e30sInRlbmFudElkIjoiOWRlZjkzOTEtOWQyNC00NDg4LTg2N2UtNTk0Y2ZjMzVkMjU0IiwidXNlcklkIjo0NiwidmlzaXRvciI6ZmFsc2V9.YlWyVt6h7bsVk3u4ITCml%2BspWE4%3D";
        String encryptKey = "7xVtRkGpLq9Yfz2wM8nJcB4hDmK3Td6AeXqP1oW0ySvF5U=";


        this.authenticator.authenticate(token,"7xVtRkGpLq9Yfz2wM8nJcB4hDmK3Td6AeXqP1oW0ySvF5U=");
        System.out.println(sign);
    }
}

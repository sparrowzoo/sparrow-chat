package com.sparrow.chat.contact.test;

import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.chat.contact.service.SecretService;
import com.sparrow.passport.domain.DomainRegistry;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * DependencyInjectionTestExecutionListener
 * 在测试类中 在容器启动前执行需要定义一个TestExecutionListener
 * SparrowTestExecutionListener
 * 但在测试类中使用@Autowired注入的bean为null
 * 需要配置DependencyInjectionTestExecutionListener 提供支持
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationBoot.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SecurityTest {

    @Autowired
    private SecretService secretService;

    @Autowired
    private DomainRegistry domainRegistry;


    @Test
    public void testUserSecret() throws BusinessException {
        UserProfileDTO userProfile = new UserProfileDTO();
        userProfile.setUserId(1L);
        userProfile.setUserName("test");
        userProfile.setNickName("nick-name");
        userProfile.setEmail("email");
        userProfile.setMobile("13777777777");
        userProfile.setLastLoginTime(System.currentTimeMillis());
        userProfile.setStatus(StatusRecord.DISABLE);
        userProfile.setAvatar("http://www.avatar.com");
        userProfile.setGmtCreate(System.currentTimeMillis());
        userProfile.setPersonalSignature("你好");
        String secretUserId = this.secretService.encryptUserIdentify(userProfile);
        Long userId = this.secretService.parseUserSecretIdentify(secretUserId);
        System.out.println(userId);
    }

    @Test
    public void passwordTest() {
        System.out.println(domainRegistry.getEncryptionService().encryptPassword("abcABC123!"));
    }
}


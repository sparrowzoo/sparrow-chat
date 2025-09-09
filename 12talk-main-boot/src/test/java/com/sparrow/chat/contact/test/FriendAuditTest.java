package com.sparrow.chat.contact.test;

import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.service.AuditService;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.LoginUser;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationBoot.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class FriendAuditTest {

    @Inject
    private AuditService auditService;

    @Test
    public void testFriendAudit() throws Throwable {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUserId(1L);
        SessionContext.bindLoginUser(loginUser);
        FriendAuditParam friendAuditParam = new FriendAuditParam();
        friendAuditParam.setAuditId(1L);
        friendAuditParam.setAgree(true);
        friendAuditParam.setReason("申请理由");
        this.auditService.auditFriendApply(friendAuditParam);
    }
}

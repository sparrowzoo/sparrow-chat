package com.sparrow.chat.contact.test;

import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.chat.domain.repository.SessionRepository;
import com.sparrow.protocol.BusinessException;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationBoot.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class SessionSyncTest {
    @Autowired
    private SessionRepository sessionRepository;
    @Test
    public void test() throws BusinessException {
       // this.sessionRepository.syncSessions();
    }
}

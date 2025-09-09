package com.sparrow.chat.qun.test;

import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.service.QunService;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.BusinessException;
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
public class QunTest {

    @Inject
    private QunService qunService;

    @Test
    public void testCreateQun() throws BusinessException {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUserId(1L);
        loginUser.setUserName("mock-user-name");
        SessionContext.bindLoginUser(loginUser);

        QunCreateParam qunCreateParam = new QunCreateParam();
        qunCreateParam.setName("test");
        qunCreateParam.setAnnouncement("公告");
        qunCreateParam.setNationalityId(Nationality.CHINA.getId());
        //qunCreateParam.setOrganizationId();
        qunCreateParam.setOrganizationId(1L);
        qunCreateParam.setRemark("备注");
        qunCreateParam.setCategoryId(1L);
        this.qunService.createQun(qunCreateParam);
    }

    @Test
    public void testModifyQun() throws BusinessException {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUserId(1L);
        loginUser.setUserName("mock-user-name");
        SessionContext.bindLoginUser(loginUser);

        QunModifyParam qunModifyParam = new QunModifyParam();
        qunModifyParam.setName("test");
        qunModifyParam.setQunId(1L);
        qunModifyParam.setAnnouncement("公告");
        qunModifyParam.setRemark("备注");
        this.qunService.modify(qunModifyParam);
    }
}

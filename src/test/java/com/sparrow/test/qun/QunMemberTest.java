package com.sparrow.test.qun;

import com.sparrow.chat.dao.QunMemberDAO;
import com.sparrow.chat.po.Qun;
import com.sparrow.chat.po.QunMember;
import com.sparrow.spring.starter.test.TestWithoutBootstrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestWithoutBootstrap
public class QunMemberTest {
    @Autowired
    private QunMemberDAO qunMemberDao;

    @Test
    public void insertMember() {
        QunMember qunMember = new QunMember();
        qunMember.setQunId(2L);
        qunMember.setMemberId(1L);
        qunMember.setApplyTime(System.currentTimeMillis());
        qunMember.setAuditTime(System.currentTimeMillis());
        qunMember.setCreateTime(System.currentTimeMillis());
        this.qunMemberDao.insert(qunMember);
    }

    @Test
    public void deleteMember() {
        this.qunMemberDao.delete(1L);
    }

    @Test
    public void get() {
        QunMember qun = this.qunMemberDao.getEntity(2L);
        Assert.assertEquals(2L, (long) qun.getId());
    }
}

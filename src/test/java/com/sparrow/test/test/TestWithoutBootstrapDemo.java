package com.sparrow.test.test;

import com.sparrow.chat.dao.QunDAO;
import com.sparrow.chat.po.Qun;
import com.sparrow.spring.starter.test.TestWithoutBootstrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestWithoutBootstrap
public class TestWithoutBootstrapDemo {
    @Autowired
    private QunDAO qunDao;

    @Test
    public void insertTest() {
        Qun qun = new Qun();
        qun.setId(0L);
        qun.setName("QUN-NAME");
        qun.setLastUserId(0L);
        qun.setAnnouncement("群公告");
        qun.setNationality("china");
        qun.setInstitutionId(1L);
        qun.setCreateUserId(1L);
        qun.setUpdateUserId(1L);
        qun.setCreateTime(System.currentTimeMillis());
        qun.setUpdateTime(System.currentTimeMillis());
        qun.setRemark("群备注");
        qunDao.insert(qun);
    }
}
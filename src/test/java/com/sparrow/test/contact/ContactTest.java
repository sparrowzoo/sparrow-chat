package com.sparrow.test.contact;

import com.sparrow.chat.dao.ContactDAO;
import com.sparrow.chat.po.Contact;
import com.sparrow.spring.starter.test.TestWithoutBootstrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestWithoutBootstrap
public class ContactTest {
    @Autowired
    private ContactDAO contactDao;

    @Test
    public void deleteTest() {
        this.contactDao.delete(1L);
    }

    @Test
    public void insertTest() {
        Contact contact = new Contact();
        contact.setUserId(2L);
        contact.setFriendId(1L);
        contact.setApplyTime(System.currentTimeMillis());
        contact.setAuditTime(0L);
        contact.setCreateTime(System.currentTimeMillis());
        this.contactDao.insert(contact);
    }
}

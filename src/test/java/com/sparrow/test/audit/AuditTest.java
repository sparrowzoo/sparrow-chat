package com.sparrow.test.audit;

import com.sparrow.chat.dao.AuditDAO;
import com.sparrow.chat.enums.BusinessType;
import com.sparrow.chat.po.Audit;
import com.sparrow.spring.starter.test.TestWithoutBootstrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestWithoutBootstrap
public class AuditTest {
    @Autowired
    private AuditDAO auditDao;

    public static void testEnumSafe(BusinessType businessType) {
        System.out.println(businessType.getBusinessType());
    }

    public static void testEnumSafe(int businessType) {
        System.out.println(businessType);
    }

    public static void main(String[] args) {
        testEnumSafe(BusinessType.FRIEND);
        testEnumSafe(23);
    }

    @Test
    public void insertTest() {
        Audit audit = new Audit();
        audit.setUserId(2L);
        audit.setBusinessType(BusinessType.FRIEND);
        audit.setBusinessId(2L);
        audit.setAuditUserId(2L);
        audit.setApplyReason("你好，我是你的同学");
        audit.setAuditReason("不好意思， 不认识");
        audit.setStatus(2);
        audit.setAuditTime(System.currentTimeMillis());
        audit.setCreateTime(System.currentTimeMillis());
        this.auditDao.insert(audit);
    }
}

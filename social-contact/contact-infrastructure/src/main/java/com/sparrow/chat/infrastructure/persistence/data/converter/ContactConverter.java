package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.po.Contact;

import javax.inject.Named;

@Named
public class ContactConverter {
    public Contact convert2po(AuditBO auditBO) {
        Contact contact = new Contact();
        contact.setUserId(auditBO.getApplyUserId());
        contact.setFriendId(auditBO.getBusinessId());
        contact.setApplyTime(auditBO.getApplyTime());
        contact.setAuditTime(auditBO.getAuditTime());
        return contact;
    }
}

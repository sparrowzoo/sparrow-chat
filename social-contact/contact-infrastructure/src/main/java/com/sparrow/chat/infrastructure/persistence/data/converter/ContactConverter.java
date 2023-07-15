package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.po.Contact;

import javax.inject.Named;

@Named
public class ContactConverter {
    public Contact convert2MyPo(AuditBO auditBO) {
        Contact myContact = new Contact();
        myContact.setUserId(auditBO.getApplyUserId());
        myContact.setFriendId(auditBO.getBusinessId());
        myContact.setApplyTime(auditBO.getApplyTime());
        myContact.setAuditTime(System.currentTimeMillis());
        return myContact;
    }

    public Contact convert2FriendPo(AuditBO auditBO) {

        Contact friendContact = new Contact();
        friendContact.setUserId(auditBO.getApplyUserId());
        friendContact.setFriendId(auditBO.getBusinessId());
        friendContact.setApplyTime(auditBO.getApplyTime());
        friendContact.setAuditTime(System.currentTimeMillis());
        return friendContact;
    }
}

package com.sparrow.chat.infrastructure.converter;

import com.sparrow.chat.po.Audit;
import com.sparrow.chat.po.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactConverter {
    public Contact toCurrentContact(Audit audit) {
        Contact currentUser = new Contact();
        currentUser.setAuditTime(audit.getAuditTime());
        currentUser.setApplyTime(audit.getCreateTime());
        currentUser.setUserId(audit.getUserId());
        currentUser.setFriendId(audit.getBusinessId());
        currentUser.setCreateTime(System.currentTimeMillis());
        return currentUser;
    }

    public Contact toFriendContact(Audit audit) {
        Contact friend = new Contact();
        friend.setAuditTime(audit.getAuditTime());
        friend.setApplyTime(audit.getCreateTime());
        friend.setUserId(audit.getBusinessId());
        friend.setFriendId(audit.getUserId());
        friend.setCreateTime(System.currentTimeMillis());
        return friend;
    }
}

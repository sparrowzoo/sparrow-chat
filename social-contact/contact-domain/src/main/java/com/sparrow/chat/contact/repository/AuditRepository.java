package com.sparrow.chat.contact.repository;


import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;

import java.util.List;

public interface AuditRepository {
    Long applyFriend(FriendApplyBO friendApply);

    List<AuditBO> getFriendAuditList(Long userId);

    //    Long applyQun(Long currentUserId, QunApplyParam qunApplyParam);
//
    Integer auditFriend(AuditBO auditBO, FriendAuditParam friendAuditParam);

    AuditBO getAudit(Long auditId);
}

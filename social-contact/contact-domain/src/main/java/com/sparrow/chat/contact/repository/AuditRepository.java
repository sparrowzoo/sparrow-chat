package com.sparrow.chat.contact.repository;


import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;

import java.util.List;

public interface AuditRepository {
    Long applyFriend(FriendApplyBO friendApply);

    AuditWrapBO getFriendList(Long userId);


    AuditWrapBO getQunMemberList(Long userId);

    Long joinQun(JoinQunParam joinQun);


    Integer auditFriend(AuditBO auditBO, FriendAuditParam friendAuditParam);

    Integer auditQun(AuditBO auditBO, QunAuditParam friendAuditParam);

    AuditBO getAudit(Long auditId);

    void  changeOwner(Long qunId, Long userId);
}

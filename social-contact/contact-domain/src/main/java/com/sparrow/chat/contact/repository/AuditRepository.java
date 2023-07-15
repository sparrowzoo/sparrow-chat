package com.sparrow.chat.contact.repository;


import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;

import java.util.List;

public interface AuditRepository {
    Long applyFriend(FriendApplyBO friendApply);

    List<AuditBO> getAuditingFriendList(Long userId);


    List<AuditBO> getAuditingQunMemberList(Long qunId);


    Long joinQun(JoinQunParam joinQun);


    Integer auditFriend(AuditBO auditBO, FriendAuditParam friendAuditParam);

    Integer auditQun(AuditBO auditBO, QunAuditParam friendAuditParam);

    AuditBO getAudit(Long auditId);
}

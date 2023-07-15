package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;

import javax.inject.Named;

@Named
public class QunMemberConverter {
    public QunMember convert2QunMember(AuditBO auditBo) {
        LoginUser loginUser = ThreadContext.getLoginToken();
        QunMember qunMember = new QunMember();
        qunMember.setQunId(auditBo.getBusinessId());
        qunMember.setMemberId(loginUser.getUserId());
        qunMember.setAuditTime(System.currentTimeMillis());
        qunMember.setApplyTime(auditBo.getApplyTime());
        return qunMember;
    }

    public QunMember convert2QunMember(Long qunId) {
        LoginUser loginUser = ThreadContext.getLoginToken();
        QunMember qunMember = new QunMember();
        qunMember.setQunId(qunId);
        qunMember.setMemberId(loginUser.getUserId());
        qunMember.setAuditTime(System.currentTimeMillis());
        qunMember.setApplyTime(System.currentTimeMillis());
        return qunMember;
    }
}

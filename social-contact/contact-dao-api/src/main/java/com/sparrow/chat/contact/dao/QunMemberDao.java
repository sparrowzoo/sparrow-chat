package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.protocol.dao.DaoSupport;

public interface QunMemberDao extends DaoSupport<QunMember, Long> {
    void removeMember(Long qunId, Long memberId);

    void dissolve(Long qunId);

    Boolean isMember(Long qunId,Long memberId);


}


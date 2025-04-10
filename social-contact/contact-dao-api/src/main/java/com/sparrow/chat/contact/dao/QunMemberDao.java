package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;
import java.util.Set;

public interface QunMemberDao extends DaoSupport<QunMember, Long> {
    void removeMember(Long qunId, Long memberId);

    void dissolve(Long qunId);

    Boolean isMember(Long qunId, Long memberId);

    List<QunMember> members(Long qunId);



    /**
     * key memberId
     * value qunId
     *
     * @param memberId
     * @return
     */
    Set<Long> getQunsByMemberId(Long memberId);
}


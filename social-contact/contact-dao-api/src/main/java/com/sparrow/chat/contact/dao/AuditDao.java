package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.Audit;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;

public interface AuditDao extends DaoSupport<Audit, Long> {
    List<Audit> getAuditingFriendList(Long userId);

    List<Audit> getAuditingQunMemberList(Long userId);

    List<Audit> getMyApplingFriendList(Long userId);

    List<Audit> getMyApplingQunMemberList(Long userId);

    Audit exist(Audit audit);

    void  changeOwner(Long qunId, Long targetId);
}

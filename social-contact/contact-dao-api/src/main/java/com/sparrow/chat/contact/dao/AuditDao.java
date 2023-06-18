package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.Audit;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;

public interface AuditDao extends DaoSupport<Audit, Long> {
    List<Audit> getAudits(Long userId);

    Audit exist(Audit audit);
}

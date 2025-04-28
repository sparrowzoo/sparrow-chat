package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.Qun;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface QunDao extends DaoSupport<Qun, Long> {
    List<Qun> queryQunList(Long category);

    List<Qun> queryEnabledQunList();

    void transfer(Long qunId, Long newOwnerId);

    List<Qun> getQuns(Collection<Long> qunIds);

    Set<Long> getQunIdsByOwner(Long userId);

}

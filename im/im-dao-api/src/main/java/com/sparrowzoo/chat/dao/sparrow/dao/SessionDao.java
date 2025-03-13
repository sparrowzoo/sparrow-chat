package com.sparrowzoo.chat.dao.sparrow.dao;

import com.sparrow.protocol.dao.DaoSupport;
import com.sparrowzoo.chat.dao.sparrow.dao.po.Session;

import java.util.List;

public interface SessionDao extends DaoSupport<Session, Long> {
    List<Session> findById(String userId, Integer category);
}

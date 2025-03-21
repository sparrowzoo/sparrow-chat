package com.sparrow.chat.dao.sparrow;

import com.sparrow.protocol.dao.DaoSupport;
import com.sparrow.chat.im.po.Session;

import java.util.List;

public interface SessionDao extends DaoSupport<Session, Long> {
    List<Session> findByUser(String userId, Integer category);

    Boolean exist(String userId, Integer category, String sessionKey);

    Boolean read(String userId,Integer category, String sessionKey);
}

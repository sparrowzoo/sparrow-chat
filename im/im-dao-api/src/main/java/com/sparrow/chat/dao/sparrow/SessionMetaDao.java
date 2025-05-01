package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.dao.sparrow.query.session.SessionDBQuery;
import com.sparrow.chat.im.po.SessionMeta;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;

public interface SessionMetaDao  extends DaoSupport<SessionMeta, Long> {

    List<SessionMeta> querySession(SessionDBQuery sessionQuery);
}

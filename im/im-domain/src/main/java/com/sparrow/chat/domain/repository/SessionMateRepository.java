package com.sparrow.chat.domain.repository;

import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.protocol.BusinessException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SessionMateRepository {
    List<SessionMetaDTO> querySessions(SessionQuery sessionQuery);

    Map<String,SessionMetaDTO> querySession(Set<String> sessionKeys) throws BusinessException;
    void syncSessions() throws BusinessException;
}

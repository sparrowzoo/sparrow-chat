package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.SessionBO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.SessionQuery;

import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session, ChatUser currentUser);

    List<SessionDTO> getSessions(ChatUser chatUser);

    List<SessionBO> querySessions(SessionQuery sessionQuery);
    void read(SessionReadParams sessionReadParams);

    void fillLastReadTime(List<SessionDTO> sessionDTOList);
}

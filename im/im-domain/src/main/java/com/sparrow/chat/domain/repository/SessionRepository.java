package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;

import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session, ChatUser currentUser);

    List<SessionDTO> getSessions(ChatUser chatUser);

    void read(SessionReadParams sessionReadParams);
    void fillLastReadTime(List<SessionDTO> sessionDTOList);
}

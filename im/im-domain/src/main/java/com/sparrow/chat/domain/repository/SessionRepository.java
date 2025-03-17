package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.SessionReadQuery;
import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session, ChatUser currentUser);

    List<SessionDTO> getSessions(ChatUser chatUser);

    void read(SessionReadQuery messageRead, ChatUser chatUser) throws BusinessException;


    void canAccessSession(String sessionKey, ChatUser chatUser) throws BusinessException;
}

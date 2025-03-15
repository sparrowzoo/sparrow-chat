package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.protocol.query.MessageReadQuery;

import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session, ChatUser currentUser);

    List<ChatSession> getSessions(ChatUser chatUser);

    void read(MessageReadQuery messageRead, ChatUser chatUser);

}

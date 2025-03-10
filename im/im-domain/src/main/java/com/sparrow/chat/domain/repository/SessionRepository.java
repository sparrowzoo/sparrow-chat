package com.sparrow.chat.domain.repository;

import com.sparrow.chat.domain.bo.ChatSession;
import com.sparrow.chat.domain.bo.ChatUser;

import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session, ChatUser currentUser);

    List<ChatSession> getSessions(ChatUser chatUser);
}

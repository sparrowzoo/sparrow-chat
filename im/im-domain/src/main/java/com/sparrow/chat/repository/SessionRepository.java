package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.protocol.ChatUser;

import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session, ChatUser currentUser);

    List<ChatSession> getSessions(ChatUser chatUser);
}

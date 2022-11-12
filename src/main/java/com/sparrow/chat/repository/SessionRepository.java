package com.sparrow.chat.repository;

import com.sparrow.chat.protocol.ChatSession;
import java.util.List;

public interface SessionRepository {

    void saveSession(ChatSession session);

    List<ChatSession> getSessions(Integer userId);
}

package com.sparrow.chat.protocol.dto;

import com.sparrow.protocol.DTO;
import lombok.Data;

@Data
public class SessionDTO implements DTO {

    public static SessionDTO parse(String sessionKey, Long lastReadTime) {
        String chatType = sessionKey.substring(0, 1);
        String id = sessionKey.substring(1);
        SessionDTO session = new SessionDTO();
        session.setSessionKey(sessionKey);
        session.setChatType(Integer.parseInt(chatType));
        session.setId(id);
        session.setLastReadTime(lastReadTime);
        return session;
    }

    private String sessionKey;
    private int chatType;
    private String id;
    private Long lastReadTime = 0L;
    private MessageDTO lastMessage;
    private Integer unreadCount;
}

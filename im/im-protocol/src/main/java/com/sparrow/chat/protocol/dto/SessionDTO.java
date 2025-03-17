package com.sparrow.chat.protocol.dto;

import com.sparrow.protocol.VO;

public class SessionDTO implements VO {

    public SessionDTO(int chatType, String sessionKey) {
        this(chatType, sessionKey,0L);
    }
    public SessionDTO(int chatType, String sessionKey,Long lastReadTime) {
        this.chatType = chatType;
        this.sessionKey = sessionKey;
        this.lastReadTime = lastReadTime;
    }

    private int chatType;
    private String sessionKey;
    private Long lastReadTime=0L;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}

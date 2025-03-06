package com.sparrow.chat.protocol.param;

import com.sparrow.chat.protocol.ChatUser;

public class MessageCancelParam {
    private ChatUser sender;
    private String token;
    private Long clientSendTime;
    private String sessionKey;
    private Integer chatType;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Integer getChatType() {
        return chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public Long getClientSendTime() {
        return clientSendTime;
    }

    public void setClientSendTime(Long clientSendTime) {
        this.clientSendTime = clientSendTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

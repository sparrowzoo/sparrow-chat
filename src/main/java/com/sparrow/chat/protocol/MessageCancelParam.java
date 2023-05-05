package com.sparrow.chat.protocol;

public class MessageCancelParam {
    private Integer fromUserId;
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

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
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

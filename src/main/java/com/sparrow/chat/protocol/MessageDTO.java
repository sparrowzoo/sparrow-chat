package com.sparrow.chat.protocol;

import com.sparrow.chat.commons.MessageKey;

public class MessageDTO {
    private int messageType;
    private int chatType;
    private int fromUserId;
    private int targetUserId;
    private String session;
    private String content;
    private Long serverTime;
    private Long clientSendTime;

    public String getKey(){
        return new MessageKey(this.fromUserId,this.clientSendTime).key();
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public Long getClientSendTime() {
        return clientSendTime;
    }

    public void setClientSendTime(Long clientSendTime) {
        this.clientSendTime = clientSendTime;
    }

    public String json() {
        return "{" +
            "'messageType':" + messageType +
            ", 'chatType':" + chatType +
            ", 'fromUserId':" + fromUserId +
            ", 'targetUserId':" + targetUserId +
            ", 'session':'" + session + '\'' +
            ", 'content':'" + content + '\'' +
            ", 'serverTime':" + serverTime +
            ", 'clientSendTime':" + clientSendTime +
            '}';
    }
}

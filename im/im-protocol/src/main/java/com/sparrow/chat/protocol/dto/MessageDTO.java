package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.MessageKey;

public class MessageDTO {
    private int messageType;
    private int chatType;
    private int sender;
    private int receiver;
    private String session;
    private String content;
    private Long serverTime;
    private Long clientSendTime;

    public String getKey(){
        return new MessageKey(this.sender,this.clientSendTime).key();
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

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
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
            ", 'sender':" + sender +
            ", 'receiver':" + receiver +
            ", 'session':'" + session + '\'' +
            ", 'content':'" + content + '\'' +
            ", 'serverTime':" + serverTime +
            ", 'clientSendTime':" + clientSendTime +
            '}';
    }
}

package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.protocol.POJO;

public class MessageDTO implements POJO {
    /**
     * 消息类型
     */
    private int messageType;
    /**
     * 发送者
     */
    private ChatUserQuery sender;
    /**
     * 接收者
     */
    private ChatUserQuery receiver;
    /**
     * 会话
     */
    private SessionDTO session;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 服务器时间
     */
    private Long serverTime;
    /**
     * 客户端发送时间
     */
    private Long clientSendTime;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public ChatUserQuery getSender() {
        return sender;
    }

    public void setSender(ChatUserQuery sender) {
        this.sender = sender;
    }

    public ChatUserQuery getReceiver() {
        return receiver;
    }

    public void setReceiver(ChatUserQuery receiver) {
        this.receiver = receiver;
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

    public SessionDTO getSession() {
        return session;
    }

    public void setSession(SessionDTO session) {
        this.session = session;
    }
}

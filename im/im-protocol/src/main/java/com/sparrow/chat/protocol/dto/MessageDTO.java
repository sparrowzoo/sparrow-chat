package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.protocol.ChatUser;
import com.sparrow.chat.protocol.MessageKey;
import com.sparrow.protocol.POJO;

public class MessageDTO implements POJO {
    /**
     * 消息类型
     */
    private int messageType;
    /**
     * 发送者
     */
    private ChatUser sender;
    /**
     * 接收者
     */
    private ChatUser receiver;
    /**
     * 会话
     */
    private ChatSession session;
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

    public String getKey() {
        return new MessageKey(this.sender, this.clientSendTime).key();
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public ChatUser getReceiver() {
        return receiver;
    }

    public void setReceiver(ChatUser receiver) {
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

    public ChatSession getSession() {
        return session;
    }

    public void setSession(ChatSession session) {
        this.session = session;
    }
}
